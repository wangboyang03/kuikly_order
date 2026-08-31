package cn.itcast.order.tab

import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ColorStop
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.Direction
import com.tencent.kuikly.core.base.Rotate
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.GlassEffectStyle
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
                                glassEnable = ctx.attr.glassEnable
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
 * 用 View 组合绘制的 Tab 图标，避免引入额外图片资源
 */
internal class TabIcon : ComposeView<TabIconAttr, ComposeEvent>() {

    override fun createAttr(): TabIconAttr = TabIconAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this
        val iconSize = TabTheme.TAB_ICON_SIZE

        return {
            View {
                attr {
                    size(iconSize, iconSize)
                    allCenter()
                }

                when (ctx.attr.iconType) {
                    TabIconType.HOME -> {
                        // 屋顶：旋转 45° 的方块，下半部分被墙体盖住，形成尖顶
                        View {
                            attr {
                                size(13f, 13f)
                                absolutePosition(top = 1f, left = (iconSize - 13f) / 2)
                                transform(Rotate(45f))
                                borderRadius(2f)
                                backgroundColor(
                                    ctx.iconColor(ctx.attr.selected, ctx.attr.glassEnable)
                                )
                            }
                        }
                        // 墙体
                        View {
                            attr {
                                size(15f, 12f)
                                absolutePosition(bottom = 2f, left = (iconSize - 15f) / 2)
                                borderRadius(2f)
                                backgroundColor(
                                    ctx.iconColor(ctx.attr.selected, ctx.attr.glassEnable)
                                )
                            }
                        }
                    }

                    TabIconType.ORDER -> {
                        // 三行单据线条
                        val barWidths = listOf(16f, 11f, 16f)
                        for ((rowIndex, barWidth) in barWidths.withIndex()) {
                            View {
                                attr {
                                    size(barWidth, 3f)
                                    absolutePosition(
                                        top = 4f + rowIndex * 6f,
                                        left = (iconSize - barWidth) / 2
                                    )
                                    borderRadius(1.5f)
                                    backgroundColor(
                                        ctx.iconColor(ctx.attr.selected, ctx.attr.glassEnable)
                                    )
                                }
                            }
                        }
                    }

                    TabIconType.PROFILE -> {
                        // 头部
                        View {
                            attr {
                                size(8f, 8f)
                                absolutePosition(top = 2f, left = (iconSize - 8f) / 2)
                                borderRadius(4f)
                                backgroundColor(
                                    ctx.iconColor(ctx.attr.selected, ctx.attr.glassEnable)
                                )
                            }
                        }
                        // 肩部
                        View {
                            attr {
                                size(17f, 9f)
                                absolutePosition(bottom = 2f, left = (iconSize - 17f) / 2)
                                borderRadius(
                                    topLeft = 8f,
                                    topRight = 8f,
                                    bottomLeft = 3f,
                                    bottomRight = 3f
                                )
                                backgroundColor(
                                    ctx.iconColor(ctx.attr.selected, ctx.attr.glassEnable)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun iconColor(selected: Boolean, glassEnable: Boolean): Color {
        if (!selected) {
            return TabTheme.tabIconNormal
        }
        return if (glassEnable) TabTheme.brandEnd else Color.WHITE
    }
}

internal class TabIconAttr : ComposeAttr() {
    var iconType: TabIconType = TabIconType.HOME
    var selected: Boolean by observable(false)
    var glassEnable: Boolean by observable(false)
}

internal fun ViewContainer<*, *>.TabIcon(init: TabIcon.() -> Unit) {
    addChild(TabIcon(), init)
}