package cn.itcast.order.views.order.components

import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Scroller
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderCategory : ComposeView<OrderCategoryAttr, OrderCategoryEvent>() {
  override fun createAttr(): OrderCategoryAttr = OrderCategoryAttr()
  override fun createEvent(): OrderCategoryEvent = OrderCategoryEvent()

  override fun body(): ViewBuilder {
    val context = this
    return {
      Scroller {
        attr {
          height(46f)
          marginTop(13f)
          flexDirection(FlexDirection.ROW)
          showScrollerIndicator(false)
        }

        for (category in context.attr.categories) {
          View {
            attr {
              width(if (category.length > 2) 78f else 68f)
              height(33f)
              marginRight(7f)
              borderRadius(16.5f)
              backgroundColor(
                if (context.attr.selected == category) {
                  AppTheme.orderAccent
                } else {
                  AppTheme.orderCardBackground
                }
              )
              border(
                Border(
                  0.5f,
                  BorderStyle.SOLID,
                  if (context.attr.selected == category) {
                    AppTheme.orderAccent
                  } else {
                    AppTheme.orderCardBorder
                  }
                )
              )
              allCenter()
            }
            event {
              click { context.event.selectHandler?.invoke(category) }
            }
            Text {
              attr {
                text(category)
                color(
                  if (context.attr.selected == category) {
                    AppTheme.onPrimary
                  } else {
                    AppTheme.orderTextSecondary
                  }
                )
                fontSize(13f)
                fontWeight700()
              }
            }
          }
        }
      }
    }
  }
}

internal class OrderCategoryAttr : ComposeAttr() {
  var categories: List<String> = emptyList()
  var selected: String by observable("")
}

internal class OrderCategoryEvent : ComposeEvent() {
  var selectHandler: ((String) -> Unit)? = null

  fun onSelect(handler: (String) -> Unit) {
    selectHandler = handler
  }
}

internal fun ViewContainer<*, *>.orderCategory(init: OrderCategory.() -> Unit) {
  addChild(OrderCategory(), init)
}

internal class OrderEmptyCategory : ComposeView<OrderEmptyCategoryAttr, ComposeEvent>() {
  override fun createAttr(): OrderEmptyCategoryAttr = OrderEmptyCategoryAttr()
  override fun createEvent(): ComposeEvent = ComposeEvent()

  override fun body(): ViewBuilder {
    val context = this
    return {
      View {
        attr {
          height(154f)
          borderRadius(12f)
          backgroundColor(AppTheme.orderCardBackground)
          border(Border(0.5f, BorderStyle.SOLID, AppTheme.orderCardBorder))
          allCenter()
        }
        Text {
          attr {
            text("${context.attr.category}暂时没有更多菜品")
            color(AppTheme.orderTextPrimary)
            fontSize(15f)
            fontWeight700()
          }
        }
        Text {
          attr {
            marginTop(7f)
            text("换个分类看看，更多好味道正在准备中")
            color(AppTheme.orderTextSecondary)
            fontSize(12f)
          }
        }
      }
    }
  }
}

internal class OrderEmptyCategoryAttr : ComposeAttr() {
  var category: String by observable("")
}

internal fun ViewContainer<*, *>.orderEmptyCategory(init: OrderEmptyCategory.() -> Unit) {
  addChild(OrderEmptyCategory(), init)
}