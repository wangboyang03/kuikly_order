package cn.itcast.order.tab

import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ColorStop
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.Direction
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.GlassEffectStyle
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

/**
 * 底部悬浮 Tab 数据
 */
internal data class TabItem(
    val title: String,
    val iconType: TabIconType
)

internal enum class TabIconType {
    HOME,
    ORDER,
    PROFILE
}

/**
 * 仿 iOS 液态玻璃的悬浮底 Tab。
 * - iOS 26+：使用原生液态玻璃（View 的 glassEffectIOS 属性）
 * - 其他系统/平台：自动降级为半透明毛玻璃卡片
 *
 * 参考文档：
 * - docs/API/components/ios26-liquid-glass.md
 * - docs/DevGuide/compose-view.md
 */
internal class GlassTabBar : ComposeView<GlassTabBarAttr, GlassTabBarEvent>() {

    override fun createAttr(): GlassTabBarAttr = GlassTabBarAttr()

    override fun createEvent(): GlassTabBarEvent = GlassTabBarEvent()

    override fun body(): ViewBuilder {
        val ctx = this

        return {
            View {
                attr {
                    height(TabTheme.TAB_BAR_HEIGHT)
                    flexDirectionRow()
                    alignItemsCenter()
                    borderRadius(TabTheme.TAB_BAR_HEIGHT / 2)
                    if (ctx.attr.glassEnable) {
                        // iOS 26+ 走系统原生液态玻璃，不要再设置 backgroundColor，否则会覆盖玻璃效果
                        glassEffectIOS(
                            enable = true,
                            interactive = true,
                            style = GlassEffectStyle.REGULAR
                        )
                    } else {
                        // 降级方案：半透明卡片 + 阴影
                        backgroundColor(TabTheme.tabBarFallbackBackground)
                        boxShadow(BoxShadow(0f, 10f, 24f, Color(0x1F1A1C1F)))
                    }
                }

                for (index in ctx.attr.tabs.indices) {
                    View {
                        attr {
                            flex(1f)
                            height(TabTheme.TAB_BAR_HEIGHT - 12f)
                            margin(left = 4f, right = 4f)
                            allCenter()
                        }
                        event {
                            click {
                                ctx.event.tabSelectHandler?.invoke(index)
                            }
                        }

                        // 选中态胶囊
                        vif({ index == ctx.attr.selectedIndex }) {
                            View {
                                attr {
                                    absolutePositionAllZero()
                                    borderRadius((TabTheme.TAB_BAR_HEIGHT - 12f) / 2)
                                    if (ctx.attr.glassEnable) {
                                        backgroundColor(TabTheme.glassPill)
                                    } else {
                                        backgroundLinearGradient(
                                            Direction.TO_RIGHT,
                                            ColorStop(TabTheme.brandStart, 0f),
                                            ColorStop(TabTheme.brandEnd, 1f)
                                        )
                                    }
                                }
                            }
                        }

                        TabIcon {
                            attr {
                                iconType = ctx.attr.tabs[index].iconType
                                selected = index == ctx.attr.selectedIndex
                                orderedToday = ctx.attr.orderedToday
                            }
                        }

                        Text {
                            attr {
                                marginTop(2f)
                                text(ctx.attr.tabs[index].title)
                                fontSize(11f)
                                fontWeight600()
                                color(
                                    if (index == ctx.attr.selectedIndex) {
                                        ctx.attr.selectedContentColor()
                                    } else {
                                        TabTheme.tabTextNormal
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

internal class GlassTabBarAttr : ComposeAttr() {
    /** Tab 数据，静态配置，无需响应式 */
    var tabs: List<TabItem> = emptyList()
    var selectedIndex: Int by observable(0)
    /** 今日是否已下单，决定 ORDER tab 选中态用 order_full 还是 order_empty */
    var orderedToday: Boolean by observable(false)
    /** 是否启用 iOS 26+ 液态玻璃，由外部通过 PlatformUtils 判断后传入 */
    var glassEnable: Boolean = false

    /** 液态玻璃上内容用品牌色，降级卡片上内容用白色 */
    fun selectedContentColor(): Color {
        return if (glassEnable) TabTheme.brandEnd else Color.WHITE
    }
}

internal class GlassTabBarEvent : ComposeEvent() {
    var tabSelectHandler: ((Int) -> Unit)? = null

    fun onTabSelected(handler: (Int) -> Unit) {
        tabSelectHandler = handler
    }
}

internal fun ViewContainer<*, *>.GlassTabBar(init: GlassTabBar.() -> Unit) {
    addChild(GlassTabBar(), init)
}

/**
 * Tab 图标：使用 assets/common 下的矢量图（SVG）
 *
 * 资源命名约定：
 * - standard  = 未选中
 * - activated = 选中
 * - order 特殊：选中态需区分今日是否已下单
 *   - 今日已下单 → activated_order_full
 *   - 今日未下单 → activated_order_empty
 */
internal class TabIcon : ComposeView<TabIconAttr, ComposeEvent>() {

    override fun createAttr(): TabIconAttr = TabIconAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this
        val iconSize = TabTheme.TAB_ICON_SIZE

        return {
            Image {
                attr {
                    size(iconSize, iconSize)
                    // 各图标 viewBox 宽高比不同（如首页为 64.86:50），必须 contain 否则会被拉伸变形
                    resizeContain()
                    src(
                        ImageUri.commonAssets(
                            ctx.attr.iconType.iconName(
                                selected = ctx.attr.selected,
                                orderedToday = ctx.attr.orderedToday
                            )
                        )
                    )
                }
            }
        }
    }
}

internal class TabIconAttr : ComposeAttr() {
    var iconType: TabIconType = TabIconType.HOME
    var selected: Boolean by observable(false)
    /** 今日是否已下单，仅 ORDER 的选中态使用 */
    var orderedToday: Boolean by observable(false)
}

internal fun ViewContainer<*, *>.TabIcon(init: TabIcon.() -> Unit) {
    addChild(TabIcon(), init)
}

/**
 * 按 Tab 类型 + 选中态解析图标文件名
 */
private fun TabIconType.iconName(selected: Boolean, orderedToday: Boolean): String {
    return when (this) {
        TabIconType.HOME ->
            if (selected) TabIcons.ACTIVATED_HOMEPAGE else TabIcons.STANDARD_HOMEPAGE

        TabIconType.ORDER -> when {
            !selected -> TabIcons.STANDARD_ORDER
            orderedToday -> TabIcons.ACTIVATED_ORDER_FULL
            else -> TabIcons.ACTIVATED_ORDER_EMPTY
        }

        TabIconType.PROFILE ->
            if (selected) TabIcons.ACTIVATED_MINE else TabIcons.STANDARD_MINE
    }
}

private object TabIcons {
    const val STANDARD_HOMEPAGE = "icon_tabs_standard_homepage.svg"
    const val ACTIVATED_HOMEPAGE = "icon_tabs_activated_homepage.svg"
    const val STANDARD_ORDER = "icon_tabs_standard_order.svg"
    const val ACTIVATED_ORDER_EMPTY = "icon_tabs_activated_order_empty.svg"
    const val ACTIVATED_ORDER_FULL = "icon_tabs_activated_order_full.svg"
    const val STANDARD_MINE = "icon_tabs_standard_mine.svg"
    const val ACTIVATED_MINE = "icon_tabs_activated_mine.svg"
}