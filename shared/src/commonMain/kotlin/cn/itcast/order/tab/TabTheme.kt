package cn.itcast.order.tab

import com.tencent.kuikly.core.base.Color

/**
 * 底 Tab 场景统一的颜色与尺寸
 */
internal object TabTheme {

    // 背景
    val pageBackground = Color(0xFFF4F6FB)
    val cardBackground = Color(0xFFFFFFFF)
    val divider = Color(0xFFEDEFF5)

    // 品牌色
    val brandStart = Color(0xFFFF9A3D)
    val brandEnd = Color(0xFFFF4D6D)

    // 文字
    val titleColor = Color(0xFF1A1C1F)
    val bodyColor = Color(0xFF4A4F57)
    val subTitleColor = Color(0xFF9AA1AB)

    // TabBar
    val tabIconNormal = Color(0xFF9AA1AB)
    val tabTextNormal = Color(0xFF8A8F99)
    val tabBarFallbackBackground = Color(0xF7FFFFFF)
    val glassPill = Color(0x66FFFFFF)

    /** TabBar 高度 */
    const val TAB_BAR_HEIGHT = 62f
    /** TabBar 左右悬浮边距 */
    const val TAB_BAR_MARGIN = 24f
    /** TabBar 距屏幕底部距离（未含安全区） */
    const val TAB_BAR_BOTTOM = 14f
    /** 页面内容底部预留，避免被悬浮 TabBar 遮挡 */
    const val TAB_CONTENT_BOTTOM = 112f
    /** 图标区域尺寸 */
    const val TAB_ICON_SIZE = 22f
}
