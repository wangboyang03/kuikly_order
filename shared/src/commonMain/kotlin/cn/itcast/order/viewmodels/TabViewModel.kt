package cn.itcast.order.viewmodels

import cn.itcast.order.models.TabViewState
import com.tencent.kuikly.core.base.PagerScope
import com.tencent.kuikly.core.reactive.handler.observable

class TabViewModel(scope: PagerScope) {
  private var _state: TabViewState by scope.observable(TabViewState()) // 内部可读写的私有状态
  val state: TabViewState get() = _state // 暴露给View的只读状态
  val tabCount: Int get() = state.tabList.size

  /**
   * 用户点击Tab触发.
   * @param index 目标页面索引
   * @return 选中状态是否有变化
   */
  fun selectTab(index: Int): Boolean {
    val currentIndex = index.coerceIn(0, (tabCount - 1).coerceAtLeast(0))
    if (currentIndex == state.selectedIndex) return false
    _state = _state.copy(
      selectedIndex = currentIndex,
      loadedPages = state.loadedPages + currentIndex
    )
    return true
  }

  fun isPageLoaded(index: Int): Boolean = index in state.loadedPages
  
  /**
   * 更新今日下单状态
   * @param ordered
   */
  fun setOrderedToday(ordered: Boolean) {
    _state = _state.copy(orderedToday = ordered)
  }
}