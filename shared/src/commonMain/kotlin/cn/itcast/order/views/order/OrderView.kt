package cn.itcast.order.views.order

import cn.itcast.order.base.AppTheme
import cn.itcast.order.models.OrderViewState
import cn.itcast.order.views.order.components.orderCard
import cn.itcast.order.views.order.components.orderCartBar
import cn.itcast.order.views.order.components.orderCategory
import cn.itcast.order.views.order.components.orderEmptyCategory
import cn.itcast.order.views.order.components.orderMenuHeader
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.undefined
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Scroller
import com.tencent.kuikly.core.views.View

internal class OrderView : ComposeView<OrderViewAttr, OrderViewEvent>() {
  override fun createAttr(): OrderViewAttr = OrderViewAttr()
  override fun createEvent(): OrderViewEvent = OrderViewEvent()

  override fun body(): ViewBuilder {
    val context = this
    val initialState = context.attr.state ?: return { View {} }

    return {
      View {
        attr {
          flex(1f)
          backgroundColor(AppTheme.orderBackground)
        }

        Scroller {
          attr {
            absolutePosition(context.pagerData.statusBarHeight, 0f, 0f, 0f)
            showScrollerIndicator(false)
          }

          View {
            attr {
              padding(12f, 12f, context.attr.cartBottom + 68f, 12f)
            }

            orderMenuHeader {}

            orderCategory {
              attr {
                categories = initialState.categories
                selected = context.attr.state?.selectedCategory ?: ""
              }
              event {
                onSelect { category -> context.event.categorySelectHandler?.invoke(category) }
              }
            }

            vif({ context.attr.state?.dishVisible == true }) {
              orderCard {
                attr {
                  marginTop(12f)
                  dish = initialState.dish
                  count = context.attr.state?.dishCount ?: 0
                }
                event {
                  onAdd { context.event.addDishHandler?.invoke() }
                  onDecrease { context.event.decreaseDishHandler?.invoke() }
                }
              }
            }

            vif({ context.attr.state?.dishVisible == false }) {
              orderEmptyCategory {
                attr {
                  marginTop(12f)
                  category = context.attr.state?.selectedCategory ?: ""
                }
              }
            }
          }
        }

        orderCartBar {
          attr {
            absolutePosition(Float.undefined, 12f, context.attr.cartBottom, 12f)
            count = context.attr.state?.dishCount ?: 0
            total = context.attr.state?.totalPrice ?: 0
          }
          event {
            onSummaryClick { context.event.cartOpenHandler?.invoke() }
            onCheckout {
              val state = context.attr.state
              if (state?.canCheckout == true) {
                context.event.checkoutHandler?.invoke(state.dishCount, state.totalPrice)
              }
            }
          }
        }
      }
    }
  }
}

internal class OrderViewAttr : ComposeAttr() {
  var state: OrderViewState? by observable(null)
  var cartBottom: Float by observable(108f)
}

internal class OrderViewEvent : ComposeEvent() {
  var categorySelectHandler: ((String) -> Unit)? = null
  var addDishHandler: (() -> Unit)? = null
  var decreaseDishHandler: (() -> Unit)? = null
  var cartOpenHandler: (() -> Unit)? = null
  var checkoutHandler: ((Int, Int) -> Unit)? = null

  fun onCategorySelect(handler: (String) -> Unit) {
    categorySelectHandler = handler
  }

  fun onAddDish(handler: () -> Unit) {
    addDishHandler = handler
  }

  fun onDecreaseDish(handler: () -> Unit) {
    decreaseDishHandler = handler
  }

  fun onCartOpen(handler: () -> Unit) {
    cartOpenHandler = handler
  }

  fun onCheckout(handler: (count: Int, total: Int) -> Unit) {
    checkoutHandler = handler
  }
}

internal fun ViewContainer<*, *>.orderView(init: OrderView.() -> Unit) {
  addChild(OrderView(), init)
}