package cn.itcast.order.views.mine

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class MineView: ComposeView<ComposeAttr, ComposeEvent>() {
  override fun createAttr(): ComposeAttr = ComposeAttr()
  override fun createEvent(): ComposeEvent = ComposeEvent()
  override fun body(): ViewBuilder {
    val context = this

    return {
      View {
        attr {
          flex(1f)
          allCenter()
          paddingTop(context.pagerData.statusBarHeight)
          backgroundColor(Color(0xFFFFC9DE))
        }
        Text {
          attr {
            text("我的")
            fontSize(32f)
            fontWeight700()
            color(0xFF000000)
          }
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.mineView(init: MineView.() -> Unit) {
  addChild(MineView(), init)
}
