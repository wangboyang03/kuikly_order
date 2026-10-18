package cn.itcast.kuikly_order

import cn.itcast.kuikly_order.base.BasePager
import cn.itcast.kuikly_order.viewmodels.TabsViewModel
import cn.itcast.kuikly_order.views.Tabs
import cn.itcast.kuikly_order.views.homepage
import cn.itcast.kuikly_order.views.mine
import cn.itcast.kuikly_order.views.order
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.directives.vbind
import com.tencent.kuikly.core.layout.undefined
import com.tencent.kuikly.core.views.View

@Page("router", true) internal class Index: BasePager() {
  private val tabsViewModel: TabsViewModel by lazy(LazyThreadSafetyMode.NONE) { TabsViewModel(pagerId) }
  override fun body(): ViewBuilder {
    val context = this
    return {
      View {
        attr {
          flex(1f)
          backgroundColor(Color(0xFFF4F5F7L))
        }

        View {
          attr {
            flex(1f)
          }
          vbind({ context.tabsViewModel.selectedIndex }) {
            when (context.tabsViewModel.selectedIndex) {
              0 -> homepage { }
              1 -> order { }
              else -> mine { }
            }
          }
        }

        Tabs(context.tabsViewModel) {
          attr {
            absolutePosition(Float.undefined, 16f, (16f + context.pagerData.safeAreaInsets.bottom), 16f)
          }
        }
      }
    }
  }
}