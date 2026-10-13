package cn.itcast.kuikly_order.viewmodels

import cn.itcast.kuikly_order.models.TabItem
import cn.itcast.kuikly_order.models.tabsResponse
import com.tencent.kuikly.core.base.PagerScope
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.reactive.handler.observableList


internal class TabsViewModel(override val pagerId: String): PagerScope {
  var tabs by observableList<TabItem>()
  private var _selectedIndex by observable(0)
  val selectedIndex: Int get() = _selectedIndex
  init {
    tabs.addAll(tabsResponse)
  }

  fun switchTab(index: Int) {
    if (index in tabs.indices && index != selectedIndex) {
      _selectedIndex = index
    }
  }
}
