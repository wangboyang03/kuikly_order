package cn.itcast.order.views.index

import cn.itcast.order.base.BasePager
import cn.itcast.order.viewmodels.TabViewModel
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.layout.undefined
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.PageListView

@Page("main_tab", supportInLocal = true)
internal class Index : BasePager() {
  private val viewModel = TabViewModel(this)
  private var pageController: ViewRef<PageListView<*, *>>? = null // ViewRef 句柄容器
  private var pendingPageIndex: Int? = null // 用于点击切换的目标页 依赖过滤PageList途经的中间页

  override fun body(): ViewBuilder {
    val context = this
    val vm = context.viewModel
    val pageWidth = pagerData.pageViewWidth
    val tabBarInnerWidth = (pageWidth - 2f * TabTheme.TAB_BAR_MARGIN).coerceAtMost(TabTheme.TAB_BAR_MAX_WIDTH)
    val tabBarSideMargin = (pageWidth - tabBarInnerWidth) / 2f
    val tabItemWidth = tabBarInnerWidth / vm.tabCount

    return {
      attr {
        backgroundColor(TabTheme.pageBackground)
      }

      PageList {
        // 组件将句柄返回 ref类似LazyListState pageController类似鸿蒙Controller
        ref {
          context.pageController = it
        }
        attr {
          flexDirection(FlexDirection.ROW)
          pageDirection(true)
          pageItemWidth(pagerData.pageViewWidth)
          pageItemHeight(pagerData.pageViewHeight) // 页面全屏高度
          defaultPageIndex(0)
          firstContentLoadMaxIndex(vm.tabCount)
          showScrollerIndicator(false)
          keepItemAlive(true)
          scrollEnable(false) // 禁用页面滑动切换
          pagingEnable(false)
        }
        event {
          // 页面索引变化
          pageIndexDidChanged {
            val index = (it as JSONObject).optInt("index")
            // 切换时 只接受最终目标页 丢掉途经的中间页
            val target = context.pendingPageIndex
            if (target != null && index != target) {
              return@pageIndexDidChanged
            }
            context.pendingPageIndex = null
            vm.syncPageIndex(index)
          }
        }
        HomePage { }
        OrderPage { }
        MinePage { }
      }

      Tabs {
        attr {
          absolutePosition(Float.undefined, tabBarSideMargin, TabTheme.TAB_BAR_BOTTOM + pagerData.safeAreaInsets.bottom, tabBarSideMargin)
          tabs = vm.state.tabList
          selectedIndex = vm.state.selectedIndex
          orderedToday = vm.state.orderedToday
          itemWidth = tabItemWidth
        }
        event {
          onTabSelected { index ->
            // 只有选中态发生变化才翻页
            if (!vm.selectTab(index)) return@onTabSelected
            // 内容直接切换
            val scrolled = context.pageController?.view?.scrollToPageIndex(index, false) ?: false
            context.pendingPageIndex = if (scrolled) index else null
          }
        }
      }
    }
  }
}
