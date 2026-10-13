package cn.itcast.kuikly_order.models

/**
 * @property label 页签文案
 * @property normalIcon 未选中态图标
 * @property activatedIcon 选中态图标
 */
data class TabItem(
  val label: String,
  val normalIcon: String,
  val activatedIcon: String
)

val tabsResponse = listOf(
  TabItem("首页", "order_icon_normal_home.png", "order_icon_activated_home.png"),
  TabItem("下单", "order_icon_normal_cart.png", "order_icon_activated_cart.png"),
  TabItem("我的", "order_icon_normal_mine.png", "order_icon_activated_mine.png")
)