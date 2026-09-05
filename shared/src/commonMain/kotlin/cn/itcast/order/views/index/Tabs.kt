package cn.itcast.order.views.index

import cn.itcast.order.models.TabItemParams
import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.Blur
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class Tabs : ComposeView<TabsOptions, TabsEvent>() {
  // kuikly的泛型机制 属性类型和事件类型
  // 在Kotlin中 泛型在运行期是擦除的 -> reified也只能用在inline函数里 所以父类把创建动作定义成抽象方法 子类负责实现
  override fun createAttr(): TabsOptions = TabsOptions()
  override fun createEvent(): TabsEvent = TabsEvent()
  override fun body(): ViewBuilder {
    val context = this // 因为Kotlin DSL中存在大量带接收者的lambda 在不同lambda表达式中 this指向会发生变化

    return {
      View {
        attr {
          height(62f)
          flexDirection(FlexDirection.ROW)
          alignItems(FlexAlign.CENTER)
          paddingLeft(4f)
          paddingRight(4f)
          borderRadius(31f)
          border(Border(1f, BorderStyle.SOLID, AppTheme.tabBarBorder))
          boxShadow(BoxShadow(0f, 5f, 16f, AppTheme.tabBarShadow))
          touchEnable(true)
        }

        // 模糊背景
        Blur {
          attr {
            absolutePositionAllZero()
            borderRadius(31f)
            blurRadius(12.5f)
            touchEnable(false)
          }
        }

        View {
          attr {
            absolutePositionAllZero()
            borderRadius(31f)
            backgroundColor(AppTheme.tabBarBackground)
            touchEnable(false)
          }
        }

        for (index in context.attr.tabs.indices) {
          View {
            attr {
              flex(1f)
              height(54f)
              allCenter()
              touchEnable(true)
            }
            event {
              // 点击切换TabItem
              click {
                if (index != context.attr.selectedIndex) {
                  context.event.tabSelectHandler?.invoke(index)
                }
              }
            }

            // 叠上一个选中浮层
            View {
              attr {
                absolutePositionAllZero()
                borderRadius(27f)
                backgroundColor(AppTheme.tabItemSelectedBackground)
                boxShadow(BoxShadow(0f, 2f, 7f, AppTheme.tabItemSelectedShadow))
                opacity(if (index == context.attr.selectedIndex) 1f else 0f)
                touchEnable(false)
              }
            }

            // 图标角标容器
            View {
              attr {
                size(27f, 27f)
              }
              Image {
                attr {
                  size(27f, 27f)
                  // contain约束
                  resizeContain()
                  src(ImageUri.commonAssets(context.attr.tabs[index].icon(index == context.attr.selectedIndex, context.attr.orderedToday))
                  )
                }
              }
              vif({ context.attr.tabs[index].badge != null }) {
                View {
                  attr {
                    absolutePosition(top = -5f, right = -7f)
                    size(16f, 16f)
                    borderRadius(8f)
                    backgroundColor(AppTheme.tabBadgeBackground)
                    allCenter()
                  }
                  Text {
                    attr {
                      text(context.attr.tabs[index].badge ?: "")
                      fontSize(9f)
                      fontWeight700()
                      color(AppTheme.tabBadgeText)
                    }
                  }
                }
              }
            }
            Text {
              attr {
                marginTop(3f)
                text(context.attr.tabs[index].name)
                fontSize(12f)
                fontWeight600()
                color(
                  if (index == context.attr.selectedIndex) AppTheme.primary else AppTheme.tabTextNormal
                )
              }
            }
          }
        }
      }
    }
  }
}

internal class TabsOptions : ComposeAttr() {
  // Tabs数据源
  var tabs: List<TabItemParams> = emptyList()
  // 选中索引
  var selectedIndex: Int by observable(0)
  // 今日是否已下单
  var orderedToday: Boolean by observable(false)
}

internal class TabsEvent : ComposeEvent() {
  var tabSelectHandler: ((Int) -> Unit)? = null

  fun onTabSelected(handler: (Int) -> Unit) {
    tabSelectHandler = handler
  }
}

internal fun ViewContainer<*, *>.Tabs(init: Tabs.() -> Unit) {
  addChild(Tabs(), init)
}