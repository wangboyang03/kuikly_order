package cn.itcast.order.views.order.components

import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderMenuHeader : ComposeView<ComposeAttr, ComposeEvent>() {
  override fun createAttr(): ComposeAttr = ComposeAttr()
  override fun createEvent(): ComposeEvent = ComposeEvent()

  override fun body(): ViewBuilder {
    return {
      View {
        attr {
          height(150f)
        }

        View {
          attr {
            height(82f)
            flexDirection(FlexDirection.ROW)
            alignItems(FlexAlign.FLEX_START)
            justifyContentSpaceBetween()
          }

          View {
            attr {
              flex(1f)
              marginRight(10f)
            }
            Text {
              attr {
                text("吱吱洋芋的甜蜜日常")
                color(AppTheme.orderAccent)
                fontSize(11.5f)
                fontWeight700()
              }
            }
            Text {
              attr {
                marginTop(5f)
                text("欢迎您")
                color(AppTheme.orderTextPrimary)
                fontSize(21f)
                fontWeight700()
              }
            }
            Text {
              attr {
                marginTop(5f)
                text("先挑几道喜欢的，再慢慢决定谁请客。")
                color(AppTheme.orderTextSecondary)
                fontSize(13f)
                lineHeight(19f)
              }
            }
          }

          View {
            attr {
              marginTop(2f)
              minWidth(56f)
              height(29f)
              paddingLeft(9f)
              paddingRight(9f)
              borderRadius(14.5f)
              backgroundColor(AppTheme.orderCardBackground)
              border(Border(0.5f, BorderStyle.SOLID, AppTheme.orderCardBorder))
              flexDirection(FlexDirection.ROW)
              allCenter()
            }
            Text {
              attr {
                text("♡")
                color(AppTheme.orderAccent)
                fontSize(13f)
                fontWeight700()
              }
            }
            Text {
              attr {
                marginLeft(4f)
                text("2人")
                color(AppTheme.orderTextSecondary)
                fontSize(12f)
                fontWeight700()
              }
            }
          }
        }

        View {
          attr {
            marginTop(13f)
            height(55f)
            paddingLeft(14f)
            paddingRight(14f)
            borderRadius(12f)
            backgroundColor(AppTheme.orderAccentSoft)
            border(Border(0.5f, BorderStyle.SOLID, AppTheme.orderAccentBorder))
            flexDirection(FlexDirection.ROW)
            alignItems(FlexAlign.CENTER)
          }

          View {
            attr { flex(1f) }
            Text {
              attr {
                text("默认")
                color(AppTheme.orderTextSecondary)
                fontSize(11.5f)
              }
            }
            Text {
              attr {
                marginTop(4f)
                text("2 人餐桌")
                color(AppTheme.orderTextPrimary)
                fontSize(15f)
                fontWeight700()
              }
            }
          }

          View {
            attr {
              width(0.5f)
              height(28f)
              backgroundColor(AppTheme.orderAccentBorder)
            }
          }

          View {
            attr {
              flex(1f)
              paddingLeft(14f)
            }
            Text {
              attr {
                text("今日")
                color(AppTheme.orderTextSecondary)
                fontSize(11.5f)
              }
            }
            Text {
              attr {
                marginTop(4f)
                text("情侣推荐")
                color(AppTheme.orderTextPrimary)
                fontSize(15f)
                fontWeight700()
              }
            }
          }
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.orderMenuHeader(init: OrderMenuHeader.() -> Unit) {
  addChild(OrderMenuHeader(), init)
}