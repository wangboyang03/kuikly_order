package cn.itcast.order.views.order.components

import cn.itcast.order.base.AppTheme
import cn.itcast.order.models.OrderDish
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.ColorStop
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.Direction
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderCard : ComposeView<OrderCardAttr, OrderCardEvent>() {
  override fun createAttr(): OrderCardAttr = OrderCardAttr()
  override fun createEvent(): OrderCardEvent = OrderCardEvent()

  override fun body(): ViewBuilder {
    val context = this
    val dish = context.attr.dish ?: return { View {} }

    return {
      View {
        attr {
          height(154f)
          padding(11f)
          borderRadius(12f)
          backgroundColor(AppTheme.orderCardBackground)
          border(Border(0.5f, BorderStyle.SOLID, AppTheme.orderCardBorder))
          boxShadow(BoxShadow(0f, 4f, 12f, AppTheme.orderCardShadow))
          flexDirection(FlexDirection.ROW)
        }

        View {
          attr {
            size(80f, 80f)
            borderRadius(11f)
            backgroundLinearGradient(
              Direction.TO_BOTTOM_RIGHT,
              ColorStop(AppTheme.orderDishGradientStart, 0f),
              ColorStop(AppTheme.orderDishGradientEnd, 1f)
            )
            allCenter()
          }
          Text {
            attr {
              text(dish.cover)
              color(AppTheme.onPrimary)
              fontSize(14f)
              fontWeight700()
            }
          }
        }

        View {
          attr {
            flex(1f)
            marginLeft(11f)
          }

          View {
            attr {
              height(23f)
              flexDirection(FlexDirection.ROW)
              alignItems(FlexAlign.CENTER)
              justifyContentSpaceBetween()
            }
            Text {
              attr {
                flex(1f)
                text(dish.name)
                color(AppTheme.orderTextPrimary)
                fontSize(15.5f)
                fontWeight700()
                lines(1)
              }
            }
            View {
              attr {
                marginLeft(7f)
                height(21f)
                paddingLeft(7f)
                paddingRight(7f)
                borderRadius(10.5f)
                backgroundColor(AppTheme.orderAccentSoft)
                allCenter()
              }
              Text {
                attr {
                  text(dish.spicy)
                  color(AppTheme.orderAccent)
                  fontSize(10.5f)
                  fontWeight700()
                }
              }
            }
          }

          Text {
            attr {
              marginTop(5f)
              text(dish.description)
              color(AppTheme.orderTextSecondary)
              fontSize(12f)
              lineHeight(17f)
              lines(2)
            }
          }

          View {
            attr {
              height(25f)
              marginTop(6f)
              flexDirection(FlexDirection.ROW)
              alignItems(FlexAlign.CENTER)
            }
            for (tag in dish.tags) {
              View {
                attr {
                  height(21f)
                  marginRight(5f)
                  paddingLeft(7f)
                  paddingRight(7f)
                  borderRadius(10.5f)
                  backgroundColor(AppTheme.orderTagBackground)
                  allCenter()
                }
                Text {
                  attr {
                    text(tag)
                    color(AppTheme.orderTagText)
                    fontSize(10.5f)
                    fontWeight700()
                  }
                }
              }
            }
          }

          View {
            attr {
              height(32f)
              marginTop(4f)
              flexDirection(FlexDirection.ROW)
              alignItems(FlexAlign.CENTER)
              justifyContentSpaceBetween()
            }
            Text {
              attr {
                text("¥${dish.price}")
                color(AppTheme.orderPrice)
                fontSize(18f)
                fontWeight700()
              }
            }

            View {
              vif({ context.attr.count == 0 }) {
                View {
                  attr {
                    minWidth(if (dish.inStock) 32f else 54f)
                    height(32f)
                    paddingLeft(if (dish.inStock) 0f else 10f)
                    paddingRight(if (dish.inStock) 0f else 10f)
                    borderRadius(16f)
                    backgroundColor(
                      if (dish.inStock) {
                        AppTheme.orderAccent
                      } else {
                        AppTheme.orderDisabledBackground
                      }
                    )
                    allCenter()
                  }
                  event {
                    click {
                      if (dish.inStock) context.event.addHandler?.invoke()
                    }
                  }
                  Text {
                    attr {
                      text(if (dish.inStock) "+" else "售罄")
                      color(if (dish.inStock) AppTheme.onPrimary else AppTheme.orderDisabledText)
                      fontSize(if (dish.inStock) 19f else 11f)
                      fontWeight700()
                    }
                  }
                }
              }

              vif({ context.attr.count > 0 }) {
                View {
                  attr {
                    height(31f)
                    borderRadius(15.5f)
                    backgroundColor(AppTheme.orderAccentSoft)
                    flexDirection(FlexDirection.ROW)
                    alignItems(FlexAlign.CENTER)
                  }
                  View {
                    attr {
                      size(31f, 31f)
                      allCenter()
                    }
                    event { click { context.event.decreaseHandler?.invoke() } }
                    Text {
                      attr {
                        text("−")
                        color(AppTheme.orderAccent)
                        fontSize(17f)
                        fontWeight700()
                      }
                    }
                  }
                  Text {
                    attr {
                      minWidth(27f)
                      text(context.attr.count.toString())
                      color(AppTheme.orderTextPrimary)
                      fontSize(13.5f)
                      fontWeight700()
                      textAlignCenter()
                    }
                  }
                  View {
                    attr {
                      size(31f, 31f)
                      borderRadius(15.5f)
                      backgroundColor(AppTheme.orderAccent)
                      allCenter()
                    }
                    event { click { context.event.addHandler?.invoke() } }
                    Text {
                      attr {
                        text("+")
                        color(AppTheme.onPrimary)
                        fontSize(17f)
                        fontWeight700()
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

internal class OrderCardAttr : ComposeAttr() {
  var dish: OrderDish? = null
  var count: Int by observable(0)
}

internal class OrderCardEvent : ComposeEvent() {
  var addHandler: (() -> Unit)? = null
  var decreaseHandler: (() -> Unit)? = null

  fun onAdd(handler: () -> Unit) {
    addHandler = handler
  }

  fun onDecrease(handler: () -> Unit) {
    decreaseHandler = handler
  }
}

internal fun ViewContainer<*, *>.orderCard(init: OrderCard.() -> Unit) {
  addChild(OrderCard(), init)
}