package cn.itcast.order.base

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.reactive.handler.observable

/**
 * 浅色主题
 */
private object LightTheme {
  // 主色 / 主色变体
  val primary = Color(0xFF4DBE5F)        // 主色
  val primaryVariant = Color(0xFF2E9E45) // 主色加深
  val primaryContainer = Color(0xFFE3F6E8) // 主色浅容器
  val onPrimary = Color(0xFFFFFFFF)      // 主色之上的前景（文字/图标）
  val primarySoft = Color(0xFFEAF7ED)    // 主色极浅铺底

  // 背景 / 表面
  val background = Color(0xFFF6F9F6)     // 页面大背景（微绿白）
  val surface = Color(0xFFFFFFFF)        // 卡片 / 浮层表面
  val surfaceVariant = Color(0xFFEAF3EC) // 次级表面（分区底）
  val onSurface = Color(0xFF1A211C)      // 表面之上的主前景
  val onSurfaceMuted = Color(0xFF5F6B62) // 表面之上的次级前景（弱化）

  // 文本
  val textPrimary = Color(0xFF1A211C)    // 主文本
  val textSecondary = Color(0xFF5F6B62)  // 次要文本
  val textInverse = Color(0xFFFFFFFF)    // 反色文本（深底上的白字）

  // 分隔 / 边框
  val divider = Color(0xFFE2E8E3)        // 分割线
  val border = Color(0xFFD5DDD7)         // 边框 / 描边

  // 语义色（业务状态）
  val success = Color(0xFF34C759)        // 成功
  val warning = Color(0xFFFFA45C)        // 提醒 / 进行中
  val danger = Color(0xFFFF4D6D)         // 错误 / 危险
  val info = Color(0xFF4DBE5F)           // 提示 / 信息
}

/**
 * 深色主题
 */
private object DarkTheme {
  val primary = Color(0xFF5FD06F)
  val primaryVariant = Color(0xFF4DBE5F)
  val primaryContainer = Color(0xFF1E3A28)
  val onPrimary = Color(0xFF06210F)
  val primarySoft = Color(0xFF163527)

  // 背景 / 表面
  val background = Color(0xFF0E1512)      // 页面大背景（深绿黑）
  val surface = Color(0xFF16201B)         // 卡片 / 浮层表面
  val surfaceVariant = Color(0xFF1E2A23)   // 次级表面
  val onSurface = Color(0xFFE7EEE9)       // 表面之上的主前景
  val onSurfaceMuted = Color(0xFF9BAEA2)   // 表面之上的次级前景

  // 文本
  val textPrimary = Color(0xFFE7EEE9)
  val textSecondary = Color(0xFF9BAEA2)
  val textInverse = Color(0xFF0E1512)

  // 分隔 / 边框
  val divider = Color(0xFF26332B)
  val border = Color(0xFF324039)

  // 语义色（暗底下的明度适配）
  val success = Color(0xFF3CD06F)
  val warning = Color(0xFFFFB36B)
  val danger = Color(0xFFFF6B82)
  val info = Color(0xFF5FD06F)
}

object AppTheme {
  // 是否为深色模式，默认跟随系统/宿主外观。
  // 使用 observable 让页面 body() 建立响应式依赖：主题变化时自动重渲染。
  var isDark by observable(isSystemDark())

  // 切换主题（手动覆盖）
  fun setDark(dark: Boolean) {
    isDark = dark
  }

  // 主色系
  val primary get() = if (isDark) DarkTheme.primary else LightTheme.primary
  val primaryVariant get() = if (isDark) DarkTheme.primaryVariant else LightTheme.primaryVariant
  val primaryContainer get() = if (isDark) DarkTheme.primaryContainer else LightTheme.primaryContainer
  val onPrimary get() = if (isDark) DarkTheme.onPrimary else LightTheme.onPrimary
  val primarySoft get() = if (isDark) DarkTheme.primarySoft else LightTheme.primarySoft

  // 背景 / 表面
  val background get() = if (isDark) DarkTheme.background else LightTheme.background
  val surface get() = if (isDark) DarkTheme.surface else LightTheme.surface
  val surfaceVariant get() = if (isDark) DarkTheme.surfaceVariant else LightTheme.surfaceVariant
  val onSurface get() = if (isDark) DarkTheme.onSurface else LightTheme.onSurface
  val onSurfaceMuted get() = if (isDark) DarkTheme.onSurfaceMuted else LightTheme.onSurfaceMuted

  // 文本
  val textPrimary get() = if (isDark) DarkTheme.textPrimary else LightTheme.textPrimary
  val textSecondary get() = if (isDark) DarkTheme.textSecondary else LightTheme.textSecondary
  val textInverse get() = if (isDark) DarkTheme.textInverse else LightTheme.textInverse

  // 分隔 / 边框
  val divider get() = if (isDark) DarkTheme.divider else LightTheme.divider
  val border get() = if (isDark) DarkTheme.border else LightTheme.border

  // 语义色
  val success get() = if (isDark) DarkTheme.success else LightTheme.success
  val warning get() = if (isDark) DarkTheme.warning else LightTheme.warning
  val danger get() = if (isDark) DarkTheme.danger else LightTheme.danger
  val info get() = if (isDark) DarkTheme.info else LightTheme.info
}