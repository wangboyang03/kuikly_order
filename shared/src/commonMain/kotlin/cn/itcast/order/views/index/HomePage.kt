package cn.itcast.order.views.index

import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class HomePage : ComposeView<ComposeAttr, ComposeEvent>() {

    override fun createAttr(): ComposeAttr = ComposeAttr()

    override fun createEvent(): ComposeEvent = ComposeEvent()

    override fun body(): ViewBuilder {
        val ctx = this

        return {
            View {
                attr {
                    flex(1f)
                    allCenter()
                    // 背景通顶到状态栏；内容区用 padding 让开，避免被状态栏遮挡
                    paddingTop(ctx.pagerData.statusBarHeight)
                    backgroundColor(TabTheme.bgGreenApple)
                }
                Text {
                    attr {
                        text("首页")
                        fontSize(32f)
                        fontWeight700()
                        color(TabTheme.pageTitle)
                    }
                }
            }
        }
    }
}

internal fun ViewContainer<*, *>.HomePage(init: HomePage.() -> Unit) {
    addChild(HomePage(), init)
}
