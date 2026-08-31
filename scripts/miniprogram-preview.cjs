const fs = require('node:fs')
const path = require('node:path')
const { spawnSync } = require('node:child_process')
const ci = require('miniprogram-ci')

const repositoryRoot = path.resolve(__dirname, '..')
const projectPath = path.join(repositoryRoot, 'miniApp', 'dist')
const projectConfigPath = path.join(projectPath, 'project.config.json')
const localConfigPath = path.join(repositoryRoot, '.miniapp-ci.local.json')
const localConfig = fs.existsSync(localConfigPath)
  ? JSON.parse(fs.readFileSync(localConfigPath, 'utf8'))
  : {}
const privateKeyPath = process.env.MINIPROGRAM_PRIVATE_KEY || localConfig.privateKeyPath
const outputDirectory = path.join(repositoryRoot, '.miniapp-ci')
const qrcodeOutputDest = path.join(outputDirectory, 'preview-qrcode.jpg')
const shouldBuild = process.argv.includes('--build')
const shouldSaveImage = process.argv.includes('--image')

if (!privateKeyPath) {
  throw new Error('请设置 MINIPROGRAM_PRIVATE_KEY，或创建 .miniapp-ci.local.json。')
}

if (!fs.existsSync(privateKeyPath)) {
  throw new Error('MINIPROGRAM_PRIVATE_KEY 指向的私钥文件不存在。')
}

for (const requiredFile of [
  projectConfigPath,
  path.join(projectPath, 'business', 'nativevue2.js'),
  path.join(projectPath, 'lib', 'miniApp.js'),
]) {
  if (!fs.existsSync(requiredFile)) {
    throw new Error(`缺少小程序构建产物：${path.relative(repositoryRoot, requiredFile)}`)
  }
}

const projectConfig = JSON.parse(fs.readFileSync(projectConfigPath, 'utf8'))
if (!projectConfig.appid) {
  throw new Error('project.config.json 中没有 appid。')
}

fs.mkdirSync(outputDirectory, { recursive: true })

const project = new ci.Project({
  appid: projectConfig.appid,
  type: 'miniProgram',
  projectPath,
  privateKeyPath,
  ignores: [],
})

function buildMiniProgram() {
  const wrapper = process.platform === 'win32' ? 'gradlew.bat' : './gradlew'
  const wrapperPath = path.join(repositoryRoot, wrapper)
  const gradleArguments = [':miniApp:jsMiniAppProductionWebpack']
  const command = process.platform === 'win32'
    ? (process.env.ComSpec || 'cmd.exe')
    : wrapperPath
  const commandArguments = process.platform === 'win32'
    ? ['/d', '/s', '/c', `call "${wrapperPath}" ${gradleArguments.join(' ')}`]
    : gradleArguments
  const result = spawnSync(
    command,
    commandArguments,
    {
      cwd: repositoryRoot,
      stdio: 'inherit',
      windowsHide: true,
      windowsVerbatimArguments: process.platform === 'win32',
    },
  )

  if (result.error) throw result.error
  if (result.status !== 0) {
    throw new Error(`小程序 production 构建失败，退出码：${result.status}`)
  }
}

async function main() {
  if (shouldBuild) buildMiniProgram()

  const previewOptions = {
    project,
    desc: `本地预览 ${new Date().toLocaleString('zh-CN')}`,
    setting: {
      useProjectConfig: true,
    },
    robot: Number(process.env.MINIPROGRAM_CI_ROBOT || localConfig.robot || 1),
    qrcodeFormat: shouldSaveImage ? 'image' : 'terminal',
    onProgressUpdate(status) {
      const message = typeof status === 'string' ? status : status?.message
      if (message) console.log(message)
    },
  }

  if (shouldSaveImage) previewOptions.qrcodeOutputDest = qrcodeOutputDest
  await ci.preview(previewOptions)

  if (shouldSaveImage) {
    console.log(`预览二维码已生成：${qrcodeOutputDest}`)
  } else {
    console.log('🚚: 欢迎使用吱吱洋芋构建kuikly下单小程序')
  }
}

main().catch((error) => {
  console.error(error instanceof Error ? error.message : error)
  process.exit(1)
})
