package cn.itcast.order.views.index

import cn.itcast.order.models.TabItemParams
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
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
          borderRadius(62f / 2f)
          backgroundColor(Color(0xE5FFFFFF))
          boxShadow(BoxShadow(0f, 4f, 12f, Color(0x0A000000)))
          touchEnable(true)
        }

        for (index in context.attr.tabs.indices) {
          View {
            attr {
              flex(1f)
              height(50f)
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
                absolutePosition(0f, 6f, 0f, 6f) // 元素脱离标准文档流
                borderRadius(50f / 2f)
                backgroundColor(Color(0x1A000000))
                border(Border(1f, BorderStyle.SOLID, Color(0x26FFFFFF)))
                opacity(if (index == context.attr.selectedIndex) 1f else 0f)
                touchEnable(false) // 阻止浮层事件冒泡
              }
            }

            // 图标角标容器
            View {
              attr {
                size(22f, 22f)
              }
              Image {
                attr {
                  size(22f, 22f)
                  // contain约束
                  resizeContain()
                  src(ImageUri.commonAssets(context.attr.tabs[index].icon(index == context.attr.selectedIndex, context.attr.orderedToday))
                  )
                }
              }
              vif({ context.attr.tabs[index].badge != null }) {
                View {
                  attr {
                    absolutePosition(top = -3f, right = -3f)
                    size(16f, 16f)
                    borderRadius(8f)
                    backgroundColor(Color(0xFFFF4D6D))
                    allCenter()
                  }
                  Text {
                    attr {
                      text(context.attr.tabs[index].badge ?: "")
                      fontSize(9f)
                      fontWeight700()
                      color(Color.WHITE)
                    }
                  }
                }
              }
            }
            Text {
              attr {
                marginTop(2f)
                text(context.attr.tabs[index].name)
                fontSize(11f)
                fontWeight600()
                color(
                  if (index == context.attr.selectedIndex) Color(0xFFFF4D6D) else Color(0xFF8A8F99)
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