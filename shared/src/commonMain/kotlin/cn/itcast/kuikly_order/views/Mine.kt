package cn.itcast.kuikly_order.views

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexJustifyContent
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class MineView: ComposeView<MineViewAttr, MineViewEvent>() {

  override fun createEvent(): MineViewEvent {
    return MineViewEvent()
  }

  override fun createAttr(): MineViewAttr {
    return MineViewAttr()
  }

  override fun body(): ViewBuilder {
    return {
      View {
        attr {
          flex(1f)
          backgroundColor(Color.WHITE)
          alignItems(FlexAlign.CENTER)
          justifyContent(FlexJustifyContent.CENTER)
        }
        Text {
          attr {
            text("我的")
            fontSize(18f)
            color(Color(0xFF333333L))
          }
        }
      }
    }
  }
}


internal class MineViewAttr: ComposeAttr() {

}

internal class MineViewEvent: ComposeEvent() {

}

internal fun ViewContainer<*, *>.mine(init: MineView.() -> Unit) {
  addChild(MineView()) {
    attr {
      flex(1f)
    }
    init()
  }
}