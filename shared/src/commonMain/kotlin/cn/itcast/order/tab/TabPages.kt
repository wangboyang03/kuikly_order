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
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Scroller
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View
import com.tencent.kuikly.core.views.compose.Button
import cn.itcast.order.base.BridgeModule

/**
 * 首页
 */
internal class HomeTabView : ComposeView<ComposeAttr, ComposeEvent>() {

    override fun createAttr(): ComposeAttr = ComposeAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        return {
            Scroller {
                attr {
                    flex(1f)
                    backgroundColor(TabTheme.pageBackground)
                    showScrollerIndicator(false)
                }

                Text {
                    attr {
                        margin(top = 20f, left = 20f)
                        text("首页")
                        fontSize(28f)
                        fontWeight700()
                        color(TabTheme.titleColor)
                    }
                }
                Text {
                    attr {
                        margin(top = 6f, left = 20f)
                        text("吱吱洋芋 · 同城急送")
                        fontSize(13f)
                        color(TabTheme.subTitleColor)
                    }
                }

                // 运营横幅
                View {
                    attr {
                        margin(top = 16f, left = 20f, right = 20f)
                        height(118f)
                        borderRadius(20f)
                        padding(top = 22f, left = 20f)
                        backgroundLinearGradient(
                            Direction.TO_RIGHT,
                            ColorStop(TabTheme.brandStart, 0f),
                            ColorStop(TabTheme.brandEnd, 1f)
                        )
                        boxShadow(BoxShadow(0f, 10f, 20f, Color(0x40FF4D6D)))
                    }
                    Text {
                        attr {
                            text("新客首单立减 5 元")
                            fontSize(20f)
                            fontWeight700()
                            color(Color.WHITE)
                        }
                    }
                    Text {
                        attr {
                            marginTop(8f)
                            text("下单即用 · 全城 30 分钟送达")
                            fontSize(13f)
                            color(Color(0xE6FFFFFF))
                        }
                    }
                }

                // 金刚区
                View {
                    attr {
                        flexDirectionRow()
                        margin(top = 14f, left = 16f, right = 16f)
                        padding(top = 18f, bottom = 18f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    val entries = listOf("立即下单", "查订单", "优惠券", "找客服")
                    for (entry in entries) {
                        View {
                            attr {
                                flex(1f)
                                allCenter()
                            }
                            View {
                                attr {
                                    size(44f, 44f)
                                    borderRadius(15f)
                                    allCenter()
                                    backgroundLinearGradient(
                                        Direction.TO_BOTTOM,
                                        ColorStop(TabTheme.brandStart, 0f),
                                        ColorStop(TabTheme.brandEnd, 1f)
                                    )
                                }
                                Text {
                                    attr {
                                        text(entry.substring(0, 1))
                                        fontSize(18f)
                                        fontWeight700()
                                        color(Color.WHITE)
                                    }
                                }
                            }
                            Text {
                                attr {
                                    marginTop(8f)
                                    text(entry)
                                    fontSize(12f)
                                    color(TabTheme.bodyColor)
                                }
                            }
                        }
                    }
                }

                // 进行中的订单
                View {
                    attr {
                        margin(top = 14f, left = 16f, right = 16f)
                        padding(top = 16f, left = 16f, bottom = 8f, right = 16f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    Text {
                        attr {
                            text("进行中的订单")
                            fontSize(16f)
                            fontWeight600()
                            color(TabTheme.titleColor)
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "同城急送 · 下午茶"
                            value = "骑手已接单"
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "帮买 · 鲜花一束"
                            value = "待支付"
                            showDivider = false
                        }
                    }
                }

                // 底部预留，避免内容被悬浮 TabBar 遮挡
                View {
                    attr {
                        height(TabTheme.TAB_CONTENT_BOTTOM)
                    }
                }
            }
        }
    }
}

/**
 * 下单
 */
internal class OrderTabView : ComposeView<ComposeAttr, ComposeEvent>() {

    override fun createAttr(): ComposeAttr = ComposeAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this

        return {
            Scroller {
                attr {
                    flex(1f)
                    backgroundColor(TabTheme.pageBackground)
                    showScrollerIndicator(false)
                }

                Text {
                    attr {
                        margin(top = 20f, left = 20f)
                        text("下单")
                        fontSize(28f)
                        fontWeight700()
                        color(TabTheme.titleColor)
                    }
                }
                Text {
                    attr {
                        margin(top = 6f, left = 20f)
                        text("填写取送信息，一键呼叫骑手")
                        fontSize(13f)
                        color(TabTheme.subTitleColor)
                    }
                }

                // 取送信息
                View {
                    attr {
                        margin(top = 16f, left = 16f, right = 16f)
                        padding(top = 16f, left = 16f, bottom = 8f, right = 16f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    Text {
                        attr {
                            text("取送信息")
                            fontSize(16f)
                            fontWeight600()
                            color(TabTheme.titleColor)
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "取件地址"
                            value = "科技园南区 A 座"
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "收件地址"
                            value = "海岸城公寓 3 栋"
                            showDivider = false
                        }
                    }
                }

                // 物品信息
                View {
                    attr {
                        margin(top = 14f, left = 16f, right = 16f)
                        padding(top = 16f, left = 16f, bottom = 8f, right = 16f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    Text {
                        attr {
                            text("物品信息")
                            fontSize(16f)
                            fontWeight600()
                            color(TabTheme.titleColor)
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "物品类型"
                            value = "文件 / 证件"
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "重量"
                            value = "1 公斤以内"
                        }
                    }
                    TabInfoRow {
                        attr {
                            title = "取件时间"
                            value = "立即取件"
                            showDivider = false
                        }
                    }
                }

                // 预估费用
                View {
                    attr {
                        flexDirectionRow()
                        alignItemsCenter()
                        margin(top = 14f, left = 16f, right = 16f)
                        padding(18f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    Text {
                        attr {
                            flex(1f)
                            text("预估运费")
                            fontSize(15f)
                            color(TabTheme.bodyColor)
                        }
                    }
                    Text {
                        attr {
                            text("¥ 12.00")
                            fontSize(22f)
                            fontWeight700()
                            color(TabTheme.brandEnd)
                        }
                    }
                }

                Button {
                    attr {
                        margin(top = 24f, left = 16f, right = 16f)
                        size(pagerData.pageViewWidth - 32f, 48f)
                        borderRadius(24f)
                        backgroundLinearGradient(
                            Direction.TO_RIGHT,
                            ColorStop(TabTheme.brandStart, 0f),
                            ColorStop(TabTheme.brandEnd, 1f)
                        )
                        titleAttr {
                            text("立即下单")
                            fontSize(17f)
                            fontWeight600()
                            color(Color.WHITE)
                        }
                    }
                    event {
                        click {
                            ctx.getPager().acquireModule<BridgeModule>(BridgeModule.MODULE_NAME)
                                .toast("下单功能开发中")
                        }
                    }
                }

                View {
                    attr {
                        height(TabTheme.TAB_CONTENT_BOTTOM)
                    }
                }
            }
        }
    }
}

/**
 * 我的
 */
internal class ProfileTabView : ComposeView<ComposeAttr, ComposeEvent>() {

    override fun createAttr(): ComposeAttr = ComposeAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        return {
            Scroller {
                attr {
                    flex(1f)
                    backgroundColor(TabTheme.pageBackground)
                    showScrollerIndicator(false)
                }

                // 用户信息
                View {
                    attr {
                        flexDirectionRow()
                        alignItemsCenter()
                        margin(top = 24f, left = 16f, right = 16f)
                        padding(18f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    View {
                        attr {
                            size(56f, 56f)
                            borderRadius(28f)
                            allCenter()
                            backgroundLinearGradient(
                                Direction.TO_BOTTOM,
                                ColorStop(TabTheme.brandStart, 0f),
                                ColorStop(TabTheme.brandEnd, 1f)
                            )
                        }
                        Text {
                            attr {
                                text("洋")
                                fontSize(22f)
                                fontWeight700()
                                color(Color.WHITE)
                            }
                        }
                    }
                    View {
                        attr {
                            flex(1f)
                            marginLeft(14f)
                        }
                        Text {
                            attr {
                                text("吱吱洋芋用户")
                                fontSize(18f)
                                fontWeight600()
                                color(TabTheme.titleColor)
                            }
                        }
                        Text {
                            attr {
                                marginTop(6f)
                                text("138****8888")
                                fontSize(13f)
                                color(TabTheme.subTitleColor)
                            }
                        }
                    }
                }

                // 功能列表
                View {
                    attr {
                        margin(top = 14f, left = 16f, right = 16f)
                        padding(top = 8f, left = 16f, bottom = 8f, right = 16f)
                        borderRadius(18f)
                        backgroundColor(TabTheme.cardBackground)
                        boxShadow(BoxShadow(0f, 4f, 16f, Color(0x14000000)))
                    }
                    val items = listOf(
                        "我的订单" to "3 个进行中",
                        "优惠券" to "2 张可用",
                        "地址管理" to "",
                        "客服中心" to "",
                        "设置" to ""
                    )
                    for ((index, item) in items.withIndex()) {
                        TabInfoRow {
                            attr {
                                title = item.first
                                value = item.second
                                showDivider = index != items.lastIndex
                            }
                        }
                    }
                }

                View {
                    attr {
                        height(TabTheme.TAB_CONTENT_BOTTOM)
                    }
                }
            }
        }
    }
}

/**
 * 通用信息行：左侧标题 + 右侧副标题 + 可选分割线
 */
internal class TabInfoRow : ComposeView<TabInfoRowAttr, ComposeEvent>() {

    override fun createAttr(): TabInfoRowAttr = TabInfoRowAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this

        return {
            View {
                attr {
                    flexDirectionRow()
                    alignItemsCenter()
                    height(52f)
                }
                Text {
                    attr {
                        flex(1f)
                        text(ctx.attr.title)
                        fontSize(15f)
                        color(TabTheme.titleColor)
                    }
                }
                Text {
                    attr {
                        text(ctx.attr.value)
                        fontSize(13f)
                        color(TabTheme.subTitleColor)
                    }
                }
                vif({ ctx.attr.showDivider }) {
                    View {
                        attr {
                            absolutePosition(left = 0f, right = 0f, bottom = 0f)
                            height(0.5f)
                            backgroundColor(TabTheme.divider)
                        }
                    }
                }
            }
        }
    }
}

internal class TabInfoRowAttr : ComposeAttr() {
    var title: String by observable("")
    var value: String by observable("")
    var showDivider: Boolean by observable(true)
}

internal fun ViewContainer<*, *>.TabInfoRow(init: TabInfoRow.() -> Unit) {
    addChild(TabInfoRow(), init)
}

internal fun ViewContainer<*, *>.HomeTabView(init: HomeTabView.() -> Unit) {
    addChild(HomeTabView(), init)
}

internal fun ViewContainer<*, *>.OrderTabView(init: OrderTabView.() -> Unit) {
    addChild(OrderTabView(), init)
}

internal fun ViewContainer<*, *>.ProfileTabView(init: ProfileTabView.() -> Unit) {
    addChild(ProfileTabView(), init)
}