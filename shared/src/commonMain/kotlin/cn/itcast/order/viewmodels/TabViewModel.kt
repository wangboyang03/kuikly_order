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
    val target = index.coerceIn(0, (tabCount - 1).coerceAtLeast(0)) // 越界保护[0, 0]
    if (target == state.selectedIndex) return false
    setState { copy(selectedIndex = target) }
    return true
  }
  
  /**
   * 同步页面索引, PageList翻页后回调修改状态
   * @param index 目标页面索引
   */
  fun syncPageIndex(index: Int) {
    setState {
      copy(selectedIndex = index.coerceIn(0, (tabList.size - 1).coerceAtLeast(0)))
    }
  }
  
  /**
   * 更新今日下单状态
   * @param ordered
   */
  fun setOrderedToday(ordered: Boolean) {
    setState { copy(orderedToday = ordered) }
  }

  private fun setState(reducer: TabViewState.() -> TabViewState) {
    // 带接收者的函数类型, 当lambda在被调用时, this指向TabViewState实例
    _state = _state.reducer()
  }
}
