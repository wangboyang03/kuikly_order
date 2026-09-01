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
    val tabTextNormal = Color(0xFF8A8F99)
    /**
     * Tab 栏底板：90% 半透明白，靠阴影与圆角表现悬浮玻璃质感，
     * 不依赖任何原生 backdrop-filter / iOS 液态玻璃 API，所有端一致。
     */
    val tabBarBackground = Color(0xE5FFFFFF)
    /**
     * 滑动镜片（lens）：水滴玻璃效果。
     * 用 10% 黑色半透明模拟"磨砂玻璃"遮挡感，
     * 配合 1px 细边框模拟玻璃边缘高光折射线。
     * 不依赖 backdrop-filter / iOS 液态玻璃 API，所有端一致。
     */
    val lensBackground = Color(0x1A000000)
    /** 镜片细边框：模拟玻璃边缘的高光折射线 */
    val lensBorderColor = Color(0x26FFFFFF)
    /** 角标背景：品牌红色（与 brandEnd 一致） */
    val badgeBackground = Color(0xFFFF4D6D)

    /** TabBar 高度 */
    const val TAB_BAR_HEIGHT = 62f
    /** TabBar 左右悬浮边距（也是窄屏下的最小边距） */
    const val TAB_BAR_MARGIN = 40f
    /** TabBar 内部内容最大宽度，超出则居中收短，避免大屏上拉得太长 */
    const val TAB_BAR_MAX_WIDTH = 340f
    /** TabBar 距屏幕底部距离（未含安全区） */
    const val TAB_BAR_BOTTOM = 14f
    /** 页面内容底部预留，避免被悬浮 TabBar 遮挡 */
    const val TAB_CONTENT_BOTTOM = 112f
    /** 图标区域尺寸 */
    const val TAB_ICON_SIZE = 22f

    /** 滑动镜片（lens）的高度 */
    const val LENS_HEIGHT = 50f
    /** 镜片在 Tab 槽内的水平内边距（左右各一） */
    const val LENS_HORIZONTAL_INSET = 6f
    /** 镜片在 Tab 栏内的垂直内边距（顶部） */
    const val LENS_VERTICAL_INSET = 6f
}
