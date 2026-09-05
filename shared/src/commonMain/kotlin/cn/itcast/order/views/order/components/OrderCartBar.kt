package cn.itcast.order.views.order.components

import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderCartBar : ComposeView<OrderCartBarAttr, OrderCartBarEvent>() {
  override fun createAttr(): OrderCartBarAttr = OrderCartBarAttr()
  override fun createEvent(): OrderCartBarEvent = OrderCartBarEvent()

  override fun body(): ViewBuilder {
    val context = this
    return {
      View {
        attr {
          height(56f)
          paddingLeft(9f)
          paddingRight(9f)
          borderRadius(15f)
          backgroundColor(AppTheme.orderFloatingBackground)
          border(Border(0.5f, BorderStyle.SOLID, AppTheme.orderFloatingBorder))
          boxShadow(BoxShadow(0f, 8f, 22f, AppTheme.orderFloatingShadow))
          flexDirection(FlexDirection.ROW)
          alignItems(FlexAlign.CENTER)
          justifyContentSpaceBetween()
        }

        View {
          attr {
            flex(1f)
            flexDirection(FlexDirection.ROW)
            alignItems(FlexAlign.CENTER)
          }
          event { click { context.event.summaryHandler?.invoke() } }

          View {
            attr {
              size(36f, 36f)
              borderRadius(12f)
              backgroundColor(
                if (context.attr.count > 0) {
                  AppTheme.orderAccent
                } else {
                  AppTheme.orderAccentSoft
                }
              )
              allCenter()
            }
            Text {
              attr {
                text(if (context.attr.count > 0) context.attr.count.toString() else "餐")
                color(if (context.attr.count > 0) AppTheme.onPrimary else AppTheme.orderAccent)
                fontSize(if (context.attr.count > 0) 15f else 11f)
                fontWeight700()
              }
            }
          }

          View {
            attr {
              flex(1f)
              marginLeft(9f)
            }
            Text {
              attr {
                text(if (context.attr.count > 0) "已选 ${context.attr.count} 件" else "餐桌还是空的")
                color(AppTheme.orderTextPrimary)
                fontSize(14f)
                fontWeight700()
              }
            }
            Text {
              attr {
                marginTop(4f)
                text(if (context.attr.count > 0) "合计 ¥${context.attr.total}" else "挑一道你们爱吃的吧")
                color(if (context.attr.count > 0) AppTheme.orderPrice else AppTheme.orderTextSecondary)
                fontSize(11.5f)
                if (context.attr.count > 0) {
                  fontWeight700()
                } else {
                  fontWeight400()
                }
              }
            }
          }
        }

        View {
          attr {
            width(88f)
            height(38f)
            marginLeft(9f)
            borderRadius(19f)
            backgroundColor(
              if (context.attr.count > 0) {
                AppTheme.orderAccent
              } else {
                AppTheme.orderDisabledBackground
              }
            )
            allCenter()
          }
          event { click { context.event.checkoutHandler?.invoke() } }
          Text {
            attr {
              text(if (context.attr.count > 0) "去结算" else "先点餐")
              color(if (context.attr.count > 0) AppTheme.onPrimary else AppTheme.orderDisabledText)
              fontSize(13.5f)
              fontWeight700()
            }
          }
        }
      }
    }
  }
}

internal class OrderCartBarAttr : ComposeAttr() {
  var count: Int by observable(0)
  var total: Int by observable(0)
}

internal class OrderCartBarEvent : ComposeEvent() {
  var summaryHandler: (() -> Unit)? = null
  var checkoutHandler: (() -> Unit)? = null

  fun onSummaryClick(handler: () -> Unit) {
    summaryHandler = handler
  }

  fun onCheckout(handler: () -> Unit) {
    checkoutHandler = handler
  }
}

internal fun ViewContainer<*, *>.orderCartBar(init: OrderCartBar.() -> Unit) {
  addChild(OrderCartBar(), init)
}
