#!/usr/bin/env node
import { spawn, spawnSync } from 'node:child_process'
import { createRequire } from 'node:module'
import { existsSync, readFileSync, statSync } from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const require = createRequire(import.meta.url)
const __dirname = path.dirname(fileURLToPath(import.meta.url))
const ROOT = path.resolve(__dirname, '..')
const GRADLEW = path.join(ROOT, 'gradlew')
const VERSION = '1.0.0'

// Output
const COLOR = Boolean(process.stdout.isTTY) && !process.env.NO_COLOR
const paint = (code, text) => (COLOR ? `\x1b[${code}m${text}\x1b[0m` : text)
const _style = {
  bold: (text) => paint('1', text),
  dim: (text) => paint('2', text),
  gray: (text) => paint('90', text),
  red: (text) => paint('31;1', text),
  green: (text) => paint('32;1', text),
  yellow: (text) => paint('33;1', text),
}

const ANSI_RE = /\x1b\[[0-9;]*[A-Za-z]/g
const stripAnsi = (text) => text.replace(ANSI_RE, '')

const write = (text = '') => process.stdout.write(`${text}\n`)
const row = (text = '') => write(`  ${text}`)
const rule = (width = 64) => write(_style.gray('-'.repeat(width)))

const formatDuration = (ms) => (ms < 1000 ? `${ms}ms` : `${(ms / 1000).toFixed(1)}s`)
const formatSize = (bytes) => (bytes < 1048576 ? `${(bytes / 1024).toFixed(1)} KB` : `${(bytes / 1048576).toFixed(2)} MB`)

const task = (name) => write(`\n${_style.bold(`> Task :${name}`)}`)
const buildSuccessful = (ms) => write(`\n${_style.green('BUILD SUCCESSFUL')} in ${formatDuration(ms)}`)

function fail(reason, hints) {
  write(`\n${_style.red('FAILURE:')} Build failed with an exception.`)
  write(`\n${_style.bold('* What went wrong:')}`)
  for (const line of String(reason).split('\n')) row(line)
  if (hints) {
    write(`\n${_style.bold('* Try:')}`)
    for (const line of String(hints).split('\n')) row(line)
  }
  write(`\n${_style.red('BUILD FAILED')}`)
  process.exit(1)
}

/** Line-buffered reader, keeps terminal output live while filtering noise. */
function streamLines(readable, onLine) {
  let pending = ''
  readable.setEncoding('utf8')
  readable.on('data', (chunk) => {
    pending += chunk
    const lines = pending.split('\n')
    pending = lines.pop()
    for (const line of lines) onLine(line.replace(/\r$/, ''))
  })
  readable.on('end', () => {
    if (pending) onLine(pending.replace(/\r$/, ''))
  })
}

function captureFilteredOutput(shouldDrop, run) {
  const sinks = [
    { stream: process.stdout, write: process.stdout.write.bind(process.stdout), chunks: [] },
    { stream: process.stderr, write: process.stderr.write.bind(process.stderr), chunks: [] },
  ]
  const sinkFor = (chunks) => (chunk, encoding, callback) => {
    chunks.push(typeof chunk == 'string' ? chunk : Buffer.isBuffer(chunk) ? chunk.toString('utf8') : String(chunk))
    const done = typeof encoding == 'function' ? encoding : callback
    if (typeof done == 'function') done()
    return true
  }

  for (const entry of sinks) entry.stream.write = sinkFor(entry.chunks)

  const flush = () => {
    let captured = ''
    for (const entry of sinks) {
      entry.stream.write = entry.write
      const text = entry.chunks.join('')
      captured += text
      let previousBlank = false
      for (const rawLine of text.split('\n')) {
        const line = rawLine.replace(/\r$/, '')
        if (shouldDrop(stripAnsi(line))) continue
        const blank = line.trim() == ""
        if (blank && previousBlank) continue
        previousBlank = blank
        entry.write(`${line}\n`)
      }
    }
    return captured
  }

  const settle = () => new Promise((resolve) => setTimeout(resolve, 700))
  return Promise.resolve()
    .then(run)
    .then(settle)
    .then(flush, (error) => settle().then(flush).then(() => { throw error }))
}

// Arguments
const argv = process.argv.slice(2)
const MODE = argv.includes('--mode=production') || argv.includes('production') ? 'production' : 'development'
const SKIP_BUILD = argv.includes('--skip-build')
const SKIP_QRCODE = argv.includes('--no-qrcode')

// Config
function loadConfig() {
  const cfgFile = path.join(ROOT, 'miniapp.config.json')
  let cfg = {}
  if (existsSync(cfgFile)) {
    try {
      cfg = JSON.parse(readFileSync(cfgFile, 'utf8'))
    } catch (error) {
      fail(`miniapp.config.json could not be parsed. ${error.message}`, 'Fix the JSON syntax and retry.')
    }
  }
  const keyPath = (process.env.MINIAPP_PRIVATE_KEY_PATH || cfg.privateKeyPath || '').trim()
  return {
    appid: (process.env.MINIAPP_APPID || cfg.appid || '').trim(),
    privateKeyPath: keyPath ? path.resolve(ROOT, keyPath) : '',
    projectPath: path.resolve(ROOT, (process.env.MINIAPP_PROJECT_PATH || cfg.projectPath || 'miniApp/dist').trim()),
    desc: cfg.desc || 'kuikly_order preview',
  }
}

// Java
function resolveJavaHome() {
  const candidates = [
    process.env.JAVA_HOME,
    '/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home',
    '/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home',
  ]
  for (const candidate of candidates) {
    if (candidate && existsSync(path.join(candidate, 'bin', 'java'))) return candidate
  }
  const probe = spawnSync('/usr/libexec/java_home', ['-v', '17'], { encoding: 'utf8' })
  if (probe.status == 0 && probe.stdout.trim()) return probe.stdout.trim()
  return process.env.JAVA_HOME || ''
}

// Gradle
const GRADLE_NOISE = [
  /^Deprecated Gradle features were used/,
  /^You can use '--warning-mode/,
  /^For more on this, please refer to/,
  /^BUILD (SUCCESSFUL|FAILED)/,
  /^\d+ actionable tasks:/,
]

function runGradle(taskName) {
  return new Promise((resolve, reject) => {
    const env = { ...process.env }
    if (env.NODE_OPTIONS && env.NODE_OPTIONS.includes('node-language-shim')) delete env.NODE_OPTIONS
    delete env.CODEBUDDY_BROKERED_FS_HOOK_ENABLED
    const javaHome = resolveJavaHome()
    if (javaHome) env.JAVA_HOME = javaHome

    const child = spawn(GRADLEW, [taskName, '--console=plain'], { cwd: ROOT, env })
    let warnings = 0

    const handleLine = (line) => {
      if (/^w: file:/.test(line)) {
        warnings += 1
        return
      }
      if (GRADLE_NOISE.some((pattern) => pattern.test(line))) return
      if (!line.trim()) return
      if (/^> Task :/.test(line)) {
        write(`${_style.bold('>')} ${_style.bold(line.slice(2))}`)
        return
      }
      write(line)
    }

    streamLines(child.stdout, handleLine)
    streamLines(child.stderr, handleLine)

    child.on('error', (error) => reject(new Error(`Unable to launch gradlew. ${error.message}`)))
    child.on('close', (code) => {
      if (warnings > 0) row(_style.yellow(`${warnings} Kotlin warning(s) suppressed`))
      resolve(code)
    })
  })
}

async function gradleBuild() {
  const gradleTask = MODE == 'production' ? ':miniApp:jsMiniAppProductionWebpack' : ':miniApp:jsMiniAppDevelopmentWebpack'
  task(gradleTask)
  const code = await runGradle(gradleTask)
  if (code !== 0) {
    fail(
      `Execution failed for task '${gradleTask}'.`,
      'Run with --stacktrace to get the stack trace.\nRun with --info or --debug for more log output.',
    )
  }
}

// Verify
function verifyDist(cfg) {
  task('verifyArtifacts')
  const required = [
    'app.js',
    'app.json',
    'project.config.json',
    path.join('pages', 'index', 'index.js'),
    path.join('lib', 'miniApp.js'),
    path.join('business', 'nativevue2.js'),
  ]

  const entries = []
  let total = 0
  for (const relativePath of required) {
    const file = path.join(cfg.projectPath, relativePath)
    if (!existsSync(file)) {
      fail(`Artifact '${relativePath}' is missing.`, 'Run a full build without --skip-build.')
    }
    const size = statSync(file).size
    total += size
    entries.push([relativePath, size])
  }

  const label = 'main package'
  const width = Math.max(...entries.map(([name]) => name.length), label.length)
  for (const [name, size] of entries) row(`${name.padEnd(width)}  ${formatSize(size).padStart(10)}`)
  rule(width + 14)

  const limit = 2 * 1048576
  const status = total > limit ? _style.red('exceeds limit') : _style.green('within limit')
  row(`${_style.bold(label.padEnd(width))}  ${formatSize(total).padStart(10)}  ${_style.dim('/ 2.00 MB')}  ${status}`)
  if (total > limit) {
    row(_style.yellow('开发包超过2MB的主包限制.'))
  }
}

// Qrcode
const CI_NOISE = [
  /^\[summer-compiler\]/,
  /^serialize /,
  /^child process (stdout|stderr):/,
  /^\(node:\d+\)/,
  /^\(Use `node --trace-deprecation/,
  /^try to get input sourcemap/,
  /^getCodeFiles:/,
  /^\[log\]/,
  /^terminal qrcode shown above$/,
  /^\[info\]/,
  /^\[error\]/,
]

async function generateQrcode(cfg) {
  task('generatePreviewQrcode')

  if (!cfg.appid) {
    fail("未配置小程序AppID.")
  }
  if (!cfg.privateKeyPath || !existsSync(cfg.privateKeyPath)) {
    fail("未找到上传私钥")
  }

  row(`${_style.dim('appid')}   ${cfg.appid}`)
  row(`${_style.dim('mode')}    ${MODE}`)

  let result
  let failure = null
  let missing = false

  const captured = await captureFilteredOutput(
    (line) => CI_NOISE.some((pattern) => pattern.test(line)),
    async () => {
      let ci
      try {
        ci = require('miniprogram-ci')
      } catch {
        missing = true
        return
      }

      const project = new ci.Project({
        appid: cfg.appid,
        type: 'miniProgram',
        projectPath: cfg.projectPath,
        privateKeyPath: cfg.privateKeyPath,
        ignores: ['node_modules/**/*', '**/*.map'],
      })

      try {
        result = await ci.preview({
          project,
          desc: `${cfg.desc} (${MODE})`,
          setting: {
            es6: true,
            es7: true,
            minify: MODE == 'production',
            minifyJS: MODE == 'production',
            minifyWXML: MODE == 'production',
            minifyWXSS: MODE == 'production',
            autoPrefixWXSS: true,
            codeProtect: false,
          },
          qrcodeFormat: 'terminal',
          onProgressUpdate: (taskStatus) => {
            if (typeof taskStatus == 'string' && taskStatus.trim()) row(_style.dim(taskStatus))
          },
        })
      } catch (error) {
        failure = error
      }
    },
  )

  if (missing) fail('小程序预览依赖MiniProgram CI未安装.')
  if (failure) fail('MiniProgram CI服务拒绝了预览请求', explainCiError(failure))

  for (const pkg of result?.subPackageInfo ?? []) {
    const name = !pkg.name || pkg.name == '__FULL__' ? 'main' : pkg.name
    row(`${_style.dim('package')}  ${name.padEnd(10)} ${formatSize(pkg.size).padStart(10)}`)
  }
  write()

  const qrRendered = /\x1b\[4[07]m/.test(captured) || /[\u2580-\u259F]/.test(captured)
  if (qrRendered) {
    row(_style.green("请使用微信扫描二维码预览，有效期约为25分钟."))
  } else {
    row(_style.yellow("编译成功，但没有返回二维码."))
  }
}

function explainCiError(error) {
  const raw = String((error && (error.errMsg || error.message)) || error)
  const match = raw.match(/"errCode":(-?\d+),"errMsg":"([^"]*)"/)
  const code = match ? match[1] : null
  const detail = match ? match[2] : raw
  const ip = (detail.match(/invalid ip:\s*([\d.]+)/) || [])[1]

  const hints = {
    '-10008': `本机公网IPv4${ip ? ` (${ip})` : "unknown"}不再小程序白名单中?`,
    '-10001': "密钥已过期或与当前Appid不匹配?",
    '-10004': "当前Appid无法预览",
    '-80082': "软件包大小超过最大约束(2MB/20MB)",
  }
  return (code && hints[code]) || `WeChat returned: ${detail}`
}

// Main
async function main() {
  const startedAt = Date.now()
  const cfg = loadConfig()

  write(`\n${_style.bold('🍨欢迎来到吱吱洋芋の甜蜜互动下单小程序，准备好了吗，让我们一起出发吧~')} ${_style.dim(`v${VERSION}`)}`)
  rule()
  const summary = [
    ['mode', MODE],
    ['project', path.relative(ROOT, cfg.projectPath) || '.'],
    ['appid', cfg.appid || _style.yellow('<not configured>')],
    ['key', cfg.privateKeyPath ? path.relative(ROOT, cfg.privateKeyPath) : _style.yellow('<not configured>')],
  ]
  const width = Math.max(...summary.map(([key]) => key.length))
  for (const [key, value] of summary) row(`${_style.dim(key.padEnd(width))}  ${value}`)

  if (SKIP_BUILD) {
    task('build')
    row(_style.yellow('skipped (--skip-build), reusing existing dist'))
  } else {
    await gradleBuild()
  }

  verifyDist(cfg)

  if (SKIP_QRCODE) {
    task('generatePreviewQrcode')
    row(_style.yellow('skipped (--no-qrcode)'))
  } else {
    await generateQrcode(cfg)
  }
  buildSuccessful(Date.now() - startedAt)
}

main().catch((error) => fail(error?.stack || String(error)))