package cn.itcast.kuikly_order.views

import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text

internal class HomeView: ComposeView<HomeViewAttr, HomeViewEvent>() {
  override fun createEvent(): HomeViewEvent {
    return HomeViewEvent()
  }

  override fun createAttr(): HomeViewAttr {
    return HomeViewAttr()
  }

  override fun body(): ViewBuilder {
    val context = this
    return {
      Text {
        attr {
          text("首页")
        }
      }
    }
  }
}


internal class HomeViewAttr: ComposeAttr() {

}

internal class HomeViewEvent: ComposeEvent() {

}

internal fun ViewContainer<*, *>.homepage(init: HomeView.() -> Unit) {
  addChild(HomeView(), init)
}