package cn.itcast.order.views.homepage

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class HomePageView: ComposeView<ComposeAttr, ComposeEvent>() {
  override fun createAttr(): ComposeAttr = ComposeAttr()
  override fun createEvent(): ComposeEvent = ComposeEvent()
  override fun body(): ViewBuilder {
    val context = this

    return {
      View {
        attr {
          flex(1f)
          allCenter()
          paddingTop(context.pagerData.statusBarHeight) // 开启沉浸式状态栏
          backgroundColor(Color(0xFF8FD14F))
        }
        Text {
          attr {
            text("首页")
            fontSize(32f)
            fontWeight700()
            color(0xFF000000)
          }
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.homepageView(init: HomePageView.() -> Unit) {
  addChild(HomePageView(), init)
}
