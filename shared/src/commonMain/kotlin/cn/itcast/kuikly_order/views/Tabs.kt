package cn.itcast.kuikly_order.views

import cn.itcast.kuikly_order.viewmodels.TabsViewModel
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.directives.vforIndex
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class TabsView(val mViewModel: TabsViewModel): ComposeView<ComposeAttr, ComposeEvent>() {
  override fun createAttr(): ComposeAttr {
    return ComposeAttr()
  }
  override fun createEvent(): ComposeEvent {
    return ComposeEvent()
  }
  override fun body(): ViewBuilder {
    val context = this
    return {
      View {
        attr {
          flexDirection(FlexDirection.ROW)
          alignItems(FlexAlign.CENTER)
          height(68f)
          padding(6f)
          backgroundColor(Color.WHITE)
          borderRadius(68f / 2f)
          boxShadow(BoxShadow(0f, 6f, 20f, Color(0x1A000000L)), true)
        }

        vforIndex({ context.mViewModel.tabs }) { item, index, _ ->
          View {
            attr {
              flex(1f)
              height(56f)
              allCenter()
              borderRadius(56f / 2f)
              backgroundColor(if (index == context.mViewModel.selectedIndex) Color(0xFFFFF1F4L) else Color.TRANSPARENT)
            }

            event {
              click {
                context.mViewModel.switchTab(index)
              }
            }

            Image {
              attr {
                size(26f, 26f)
                resizeContain()
                src(ImageUri.commonAssets(if (index == context.mViewModel.selectedIndex) item.activatedIcon else item.normalIcon))
              }
            }

            Text {
              attr {
                text(item.label)
                fontSize(11f)
                marginTop(3f)
                if (index == context.mViewModel.selectedIndex) {
                  color(Color(0xFFFEB6C1L))
                  fontWeightSemiBold()
                } else {
                  color(Color(0xFF999999L))
                  fontWeightNormal()
                }
              }
            }
          }
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.Tabs(mViewModel: TabsViewModel, init: TabsView.() -> Unit = {}) {
  addChild(TabsView(mViewModel), init)
}