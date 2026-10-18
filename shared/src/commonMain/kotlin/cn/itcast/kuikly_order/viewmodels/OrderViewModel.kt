package cn.itcast.kuikly_order.viewmodels

import com.tencent.kuikly.core.base.PagerScope
import com.tencent.kuikly.core.reactive.handler.observable

internal class OrderViewModel(override val pagerId: String): PagerScope {
  private var _selectedTabIndex by observable(0)
  val selectedTabIndex: Int get() = _selectedTabIndex

  fun changeTabItem(index: Int) {
    if (index == _selectedTabIndex) return
    _selectedTabIndex = index
  }
}