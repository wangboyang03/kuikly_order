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
  val primarySoft = Color(0xFFE8F8EF)    // 主色极浅铺底

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

  // 底部标签栏
  val tabBarBackground = Color(0xB8FAFAFA)
  val tabBarBorder = Color(0x14000000)
  val tabBarShadow = Color(0x18000000)
  val tabItemSelectedBackground = Color(0x24000000)
  val tabItemSelectedShadow = Color(0x10000000)
  val tabTextNormal = Color(0xFF2E2E2E)
  val tabBadgeBackground = Color(0xFFFF4B4B)
  val tabBadgeText = Color(0xFFFFFFFF)

  // 下单页
  val orderBackground = Color(0xFFF8F8F8)
  val orderCardBackground = Color(0xFFFFFFFF)
  val orderCardBorder = Color(0xE6EEEEEE)
  val orderCardShadow = Color(0x0A26181C)
  val orderTextPrimary = Color(0xFF333333)
  val orderTextSecondary = Color(0xFF888888)
  val orderAccent = Color(0xFFFF7F9F)
  val orderAccentSoft = Color(0xFFFFF0F4)
  val orderAccentBorder = Color(0x2EFF7F9F)
  val orderPrice = Color(0xFFFF8A3D)
  val orderTagBackground = Color(0xFFFFF8ED)
  val orderTagText = Color(0xFFB96D21)
  val orderDisabledBackground = Color(0xFFEEEEEE)
  val orderDisabledText = Color(0xFF999999)
  val orderDishGradientStart = Color(0xFFF87565)
  val orderDishGradientEnd = Color(0xFFFF9F7A)
  val orderFloatingBackground = Color(0xF7FFFFFF)
  val orderFloatingBorder = Color(0xEBEEEEEE)
  val orderFloatingShadow = Color(0x1F26181C)

  // 语义色（业务状态）
  val success = Color(0xFF34C759)        // 成功
  val warning = Color(0xFFFFA45C)        // 提醒 / 进行中
  val danger = Color(0xFFFF4D6D)         // 错误 / 危险
  val info = Color(0xFF07C160)           // 提示 / 信息
}

/**
 * 深色主题
 */
private object DarkTheme {
  val primary = Color(0xFF34D477)
  val primaryVariant = Color(0xFF07C160)
  val primaryContainer = Color(0xFF153D26)
  val onPrimary = Color(0xFF06210F)
  val primarySoft = Color(0xFF123321)

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

  // 底部标签栏
  val tabBarBackground = Color(0xB31C1F24)
  val tabBarBorder = Color(0xFF414854)
  val tabBarShadow = Color(0x42000000)
  val tabItemSelectedBackground = Color(0x24FFFFFF)
  val tabItemSelectedShadow = Color(0x24000000)
  val tabTextNormal = Color(0xFF8493A5)
  val tabBadgeBackground = Color(0xFFFF4B4B)
  val tabBadgeText = Color(0xFFFFFFFF)

  // 下单页
  val orderBackground = Color(0xFF101512)
  val orderCardBackground = Color(0xFF18201B)
  val orderCardBorder = Color(0xFF29342D)
  val orderCardShadow = Color(0x50000000)
  val orderTextPrimary = Color(0xFFF1F3F1)
  val orderTextSecondary = Color(0xFF9BA59E)
  val orderAccent = Color(0xFFFF91AC)
  val orderAccentSoft = Color(0xFF3A242C)
  val orderAccentBorder = Color(0x52FF91AC)
  val orderPrice = Color(0xFFFFA15D)
  val orderTagBackground = Color(0xFF382A1D)
  val orderTagText = Color(0xFFFFBE78)
  val orderDisabledBackground = Color(0xFF29302C)
  val orderDisabledText = Color(0xFF7F8982)
  val orderDishGradientStart = Color(0xFFC95E55)
  val orderDishGradientEnd = Color(0xFFE58E6D)
  val orderFloatingBackground = Color(0xF21A211D)
  val orderFloatingBorder = Color(0xFF313C35)
  val orderFloatingShadow = Color(0x70000000)

  // 语义色（暗底下的明度适配）
  val success = Color(0xFF3CD06F)
  val warning = Color(0xFFFFB36B)
  val danger = Color(0xFFFF6B82)
  val info = Color(0xFF34D477)
}

object AppTheme {
  // 是否为深色模式，默认跟随系统/宿主外观。
  // 使用 observable 让页面 body() 建立响应式依赖：主题变化时自动重渲染。
  var isDark by observable(isSystemDark())
    private set

  // 切换主题（手动覆盖）
  fun updateDark(dark: Boolean) {
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

  // 底部标签栏
  val tabBarBackground get() = if (isDark) DarkTheme.tabBarBackground else LightTheme.tabBarBackground
  val tabBarBorder get() = if (isDark) DarkTheme.tabBarBorder else LightTheme.tabBarBorder
  val tabBarShadow get() = if (isDark) DarkTheme.tabBarShadow else LightTheme.tabBarShadow
  val tabItemSelectedBackground get() = if (isDark) DarkTheme.tabItemSelectedBackground else LightTheme.tabItemSelectedBackground
  val tabItemSelectedShadow get() = if (isDark) DarkTheme.tabItemSelectedShadow else LightTheme.tabItemSelectedShadow
  val tabTextNormal get() = if (isDark) DarkTheme.tabTextNormal else LightTheme.tabTextNormal
  val tabBadgeBackground get() = if (isDark) DarkTheme.tabBadgeBackground else LightTheme.tabBadgeBackground
  val tabBadgeText get() = if (isDark) DarkTheme.tabBadgeText else LightTheme.tabBadgeText

  // 下单页
  val orderBackground get() = if (isDark) DarkTheme.orderBackground else LightTheme.orderBackground
  val orderCardBackground get() = if (isDark) DarkTheme.orderCardBackground else LightTheme.orderCardBackground
  val orderCardBorder get() = if (isDark) DarkTheme.orderCardBorder else LightTheme.orderCardBorder
  val orderCardShadow get() = if (isDark) DarkTheme.orderCardShadow else LightTheme.orderCardShadow
  val orderTextPrimary get() = if (isDark) DarkTheme.orderTextPrimary else LightTheme.orderTextPrimary
  val orderTextSecondary get() = if (isDark) DarkTheme.orderTextSecondary else LightTheme.orderTextSecondary
  val orderAccent get() = if (isDark) DarkTheme.orderAccent else LightTheme.orderAccent
  val orderAccentSoft get() = if (isDark) DarkTheme.orderAccentSoft else LightTheme.orderAccentSoft
  val orderAccentBorder get() = if (isDark) DarkTheme.orderAccentBorder else LightTheme.orderAccentBorder
  val orderPrice get() = if (isDark) DarkTheme.orderPrice else LightTheme.orderPrice
  val orderTagBackground get() = if (isDark) DarkTheme.orderTagBackground else LightTheme.orderTagBackground
  val orderTagText get() = if (isDark) DarkTheme.orderTagText else LightTheme.orderTagText
  val orderDisabledBackground get() = if (isDark) DarkTheme.orderDisabledBackground else LightTheme.orderDisabledBackground
  val orderDisabledText get() = if (isDark) DarkTheme.orderDisabledText else LightTheme.orderDisabledText
  val orderDishGradientStart get() = if (isDark) DarkTheme.orderDishGradientStart else LightTheme.orderDishGradientStart
  val orderDishGradientEnd get() = if (isDark) DarkTheme.orderDishGradientEnd else LightTheme.orderDishGradientEnd
  val orderFloatingBackground get() = if (isDark) DarkTheme.orderFloatingBackground else LightTheme.orderFloatingBackground
  val orderFloatingBorder get() = if (isDark) DarkTheme.orderFloatingBorder else LightTheme.orderFloatingBorder
  val orderFloatingShadow get() = if (isDark) DarkTheme.orderFloatingShadow else LightTheme.orderFloatingShadow

  // 语义色
  val success get() = if (isDark) DarkTheme.success else LightTheme.success
  val warning get() = if (isDark) DarkTheme.warning else LightTheme.warning
  val danger get() = if (isDark) DarkTheme.danger else LightTheme.danger
  val info get() = if (isDark) DarkTheme.info else LightTheme.info
}