package cn.itcast.order.views.index

import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class MinePage : ComposeView<ComposeAttr, ComposeEvent>() {

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
                    backgroundColor(TabTheme.bgLightPink)
                }
                Text {
                    attr {
                        text("我的")
                        fontSize(32f)
                        fontWeight700()
                        color(TabTheme.pageTitle)
                    }
                }
            }
        }
    }
}

internal fun ViewContainer<*, *>.MinePage(init: MinePage.() -> Unit) {
    addChild(MinePage(), init)
}
