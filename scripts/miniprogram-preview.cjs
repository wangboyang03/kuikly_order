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

fs.mkdirSync(outputDirectory, { recursive: true })

function checkBuildArtifacts() {
  for (const requiredFile of [
    projectConfigPath,
    path.join(projectPath, 'business', 'nativevue2.js'),
    path.join(projectPath, 'lib', 'miniApp.js'),
  ]) {
    if (!fs.existsSync(requiredFile)) {
      throw new Error(`缺少小程序构建产物：${path.relative(repositoryRoot, requiredFile)}`)
    }
  }
}

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

  checkBuildArtifacts()

  const projectConfig = JSON.parse(fs.readFileSync(projectConfigPath, 'utf8'))
  if (!projectConfig.appid) {
    throw new Error('project.config.json 中没有 appid。')
  }

  const project = new ci.Project({
    appid: projectConfig.appid,
    type: 'miniProgram',
    projectPath,
    privateKeyPath,
    ignores: [],
  })

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

function describeError(error) {
  const raw = error instanceof Error ? error.message : String(error)
  const match = raw.match(/\{[\s\S]*\}/)
  if (match) {
    try {
      const payload = JSON.parse(match[0])

      if (payload.errCode === -10008) {
        const ip = /invalid ip:\s*([\d.]+)/i.exec(raw)?.[1]

        return [
          "欢迎来到吱吱洋芋的温暖小窝~",
          `当前设备出口IP地址${ip || '未知'}不在小程序代码上传白名单中，此开关仅在实验研发中保护打开，上线后将关闭。`,
          '请到小程序后台(https://mp.weixin.qq.com)-> 开发管理-> 开发设置-> 小程序代码上传中添加白名单（需小程序管理员微信扫码验证）。',
        ].join('\n')
      }
    } catch {
      return raw
    }
  }
  return raw
}

main().catch((error) => {
  console.error(describeError(error))
  process.exit(1)
})
