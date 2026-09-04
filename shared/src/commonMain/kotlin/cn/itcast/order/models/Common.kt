package cn.itcast.order.models

/**
 * 程序底Tab模型配置
 * @param name 标签名
 * @param normalIcon 未激活态图标
 * @param activatedIcon 激活态图标
 * @param specialOperationIcon 特殊运营图标
 * @param badge 角标
 */
data class TabItemParams(
  val name: String,
  val normalIcon: String,
  val activatedIcon: String,
  val specialOperationIcon: String? = null,
  val badge: String? = null
) {
  fun icon(selected: Boolean, orderedToday: Boolean): String = when {
    !selected -> normalIcon
    orderedToday && specialOperationIcon != null -> specialOperationIcon
    else -> activatedIcon
  }
}

/**
 * Tab视图状态
 * @param tabList Tabs数据源
 * @param selectedIndex 当前选中索引
 * @param orderedToday 今日是否已下单 后续业务维护
 * @param loadedPages 已加载页签下标集合
 */
data class TabViewState(
  val tabList: List<TabItemParams> = listOf(
    TabItemParams("首页", "icon_tabs_standard_homepage.svg", "icon_tabs_activated_homepage.svg"),
    TabItemParams("下单", "icon_tabs_standard_order.svg", "icon_tabs_activated_order_empty.svg", "icon_tabs_activated_order_full.svg", "1"),
    TabItemParams("我的", "icon_tabs_standard_mine.svg", "icon_tabs_activated_mine.svg")
  ),
  val selectedIndex: Int = 0,
  val orderedToday: Boolean = false,
  // 这里用了懒加载机制. 因为一次性加载所有TabItem会有很大内存开销 而且容易首屏卡帧
  // 安卓有ViewPager2/HorizontalPager iOS有UIViewController 鸿蒙TabContent
  // 原生提供的组件足以管理页面 kuikly本质上还是在声明组件树 需要业务自己管理
  val loadedPages: Set<Int> = setOf(0)
)
