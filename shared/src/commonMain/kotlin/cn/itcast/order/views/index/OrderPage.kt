package cn.itcast.order.views.index

import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderPage : ComposeView<ComposeAttr, ComposeEvent>() {

    override fun createAttr(): ComposeAttr = ComposeAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this

        return {
            View {
                attr {
                    flex(1f)
                    allCenter()
                    paddingTop(ctx.pagerData.statusBarHeight)
                    backgroundColor(TabTheme.bgSweetOrange)
                }
                Text {
                    attr {
                        text("下单")
                        fontSize(32f)
                        fontWeight700()
                        color(TabTheme.pageTitle)
                    }
                }
            }
        }
    }
}

internal fun ViewContainer<*, *>.OrderPage(init: OrderPage.() -> Unit) {
    addChild(OrderPage(), init)
}
