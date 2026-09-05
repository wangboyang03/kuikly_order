package cn.itcast.order.views.order

import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderView : ComposeView<ComposeAttr, ComposeEvent>() {
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
          backgroundColor(AppTheme.background)
        }
        Text {
          attr {
            text("下单")
            fontSize(32f)
            fontWeight700()
            color(AppTheme.textPrimary)
          }
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.orderView(init: OrderView.() -> Unit) {
  addChild(OrderView(), init)
}