package cn.itcast.order.views.index

import cn.itcast.order.base.BasePager
import cn.itcast.order.viewmodels.TabViewModel
import cn.itcast.order.views.homepage.homepageView
import cn.itcast.order.views.mine.mineView
import cn.itcast.order.views.order.orderView
import com.tencent.kuikly.core.annotations.Page
import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.layout.undefined
import com.tencent.kuikly.core.views.View

@Page("main_tab", supportInLocal = true)
internal class Index : BasePager() {
  private val viewModel = TabViewModel(this)

  override fun body(): ViewBuilder {
    val context = this
    val vm = context.viewModel
    val pageWidth = pagerData.pageViewWidth
    val tabBarInnerWidth = (pageWidth - 2f * 40f).coerceAtMost(340f)
    val tabBarSideMargin = (pageWidth - tabBarInnerWidth) / 2f
    val tabBarBottom = 14f + context.bottomSafeInset()

    return {
      attr {
        flexDirection(FlexDirection.COLUMN)
        backgroundColor(AppTheme.background)
      }

      // 内容区
      View {
        attr {
          // 铺满父容器
          absolutePositionAllZero()
        }

        for (index in vm.state.tabList.indices) {
          vif({ vm.isPageLoaded(index) && vm.state.selectedIndex == index }) {
            when (index) {
              0 -> homepageView { attr { absolutePositionAllZero() } }
              1 -> orderView { attr { absolutePositionAllZero() } }
              2 -> mineView { attr { absolutePositionAllZero() } }
            }
          }
        }
      }

      Tabs {
        attr {
          absolutePosition(Float.undefined, tabBarSideMargin, tabBarBottom, tabBarSideMargin)
          tabs = vm.state.tabList
          selectedIndex = vm.state.selectedIndex
          orderedToday = vm.state.orderedToday
        }
        event {
          onTabSelected { index -> vm.selectTab(index) }
        }
      }
    }
  }

  private fun bottomSafeInset(): Float {
    val fromFramework = pagerData.safeAreaInsets.bottom
    val fromHost = pageData.params.optString(PARAM_SAFE_AREA_BOTTOM).toFloatOrNull() ?: 0f
    return maxOf(fromFramework, fromHost, 20f)
  }

  companion object {
    /** 宿主透传的底部安全区高度参数名 */
    private const val PARAM_SAFE_AREA_BOTTOM = "safeAreaBottom"
  }
}