package cn.itcast.kuikly_order.views.components

import cn.itcast.kuikly_order.base.Constants
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.PagerScope
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.directives.vbind
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.PageListView
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

// Tabs位置
internal enum class BarPosition {
  START,
  END
}

internal typealias TabBarBuilder = ViewContainer<*, *>.(Boolean) -> Unit

/** CustomBuilder */
internal class TabBarParams(
  val items: List<TabContentSpec>,
  val state: TabsState,
  val onTabClick: (Int) -> Unit,
  val equalDivision: Boolean,
  val tabItemSpacing: Float
) {
  fun renderItems(container: ViewContainer<*, *>) {
    // 在指定容器内渲染内置页签项 复用默认布局
    container.tabBarItems(state, items, equalDivision, onTabClick, tabItemSpacing)
  }
}

/** 自定义页签栏插槽 */
internal typealias TabBarSlot = ViewContainer<*, *>.(TabBarParams) -> Unit

/** 下划线风格对象 */
internal class IndicatorStyle(
  var color: Color = Constants.MAIN_COLOR, // 下划线的颜色（默认主题色）
  var height: Float = 2f, // 下划线的高度
  var width: Float = 24f, // 下划线的宽度（0 表示不显示下划线）
  var borderRadius: Float = 0f, // 下划线的圆角半径
  var marginTop: Float = 8f // 下划线与文字的间距
)

/** 分割线样式对象 */
internal class DividerStyle(
  var strokeWidth: Float = 0f, // 分割线的线宽
  var color: Color = Color(0x33182431L), // 分割线的颜色
  var startMargin: Float = 0f, // 分割线与侧边栏顶端的距离
  var endMargin: Float = 0f // 分割线与侧边栏底端的距离
)

internal class TabsController(internal val initialIndex: Int = 0) {
  internal var barHost: TabsView? = null
  internal var sharedState: TabsState? = null

  // Tab页签切换后触发的事件
  var onChange: ((Int) -> Unit)? = null
  // Tab页签点击后触发的事件
  var onTabBarClick: ((Int) -> Unit)? = null
  // 当前显示页面的index索引 索引从0开始计算
  val currentIndex: Int get() = sharedState?.selected ?: initialIndex
  // 切换到指定页签
  fun onChangeIndex(index: Int) {
    barHost?.changeIndex(index)
  }

  // 共享选中态
  internal fun acquireState(pagerId: String, initial: Int): TabsState {
    return sharedState ?: TabsState(pagerId, initial).also {
      sharedState = it
    }
  }
}

internal class TabContentSpec(internal val content: ViewBuilder) {
  internal var barBuilder: TabBarBuilder? = null

  fun tabBar(label: String, indicator: IndicatorStyle? = null) {
    barBuilder = { selected ->
      View {
        attr {
          flexDirectionColumn()
          alignItems(FlexAlign.CENTER)
        }

        Text {
          attr {
            text(label)
            fontSize(16f)
            color(if (selected) Color(0xFF333333L) else Color(0xFF999999L))
            if (selected) {
              fontWeightSemiBold()
            } else {
              fontWeightNormal()
            }
          }
        }

        if (indicator != null) {
          View {
            attr {
              marginTop(indicator.marginTop)
              width(indicator.width)
              height(indicator.height)
              borderRadius(indicator.borderRadius)
              backgroundColor(if (selected) indicator.color else Color.TRANSPARENT)
            }
          }
        }
      }
    }
  }

  // 自定义页签
  fun tabBar(builder: TabBarBuilder) {
    barBuilder = builder
  }
}

/** Tabs内容作用域 负责收集TabContent */
internal class TabsScope {
  internal val items = mutableListOf<TabContentSpec>()
  fun TabContent(content: ViewBuilder): TabContentSpec {
    val item = TabContentSpec(content)
    items.add(item)
    return item
  }
}

/** 内部选中状态 **/
internal class TabsState(override val pagerId: String, initialIndex: Int) : PagerScope {
  var selected by observable(initialIndex)
}

/** Tabs组件参数 */
internal class TabsConfig(
  val barPosition: BarPosition,
  val equalDivision: Boolean, // Tab页签之间是否均分
  val tabItemSpacing: Float, // 页签项之间的间距
  val barWidth: Float?,
  val barHeight: Float,
  val barBackgroundColor: Color,
  val divider: DividerStyle?,
  val scrollable: Boolean, // 内容区是否允许水平滑动切换
  val animationDuration: Int,
  val pageWidth: Float?, // 每页内容宽度
  val tabBar: TabBarSlot? // 整条自定义页签栏构造器（对齐 ArkUI tabBar CustomBuilder）；为空则用内置栏
)

internal class TabsView(
  private val controller: TabsController,
  private val items: List<TabContentSpec>,
  private val config: TabsConfig,
  private val sharedState: TabsState
): ComposeView<TabsAttr, TabsEvent>() {

  private val tabsState: TabsState = sharedState

  // pageList控制器
  private var pageListRef: ViewRef<PageListView<*, *>>? = null

  internal val selectedIndex: Int get() = tabsState.selected

  override fun createAttr(): TabsAttr {
    return TabsAttr()
  }

  override fun createEvent(): TabsEvent {
    return TabsEvent()
  }

  internal fun changeIndex(index: Int) {
    if (index !in items.indices || index == tabsState.selected) return
    controller.onTabBarClick?.invoke(index)
    tabsState.selected = index
    // 同步滚动内容页
    pageListRef?.view?.scrollToPageIndex(index, true)
    controller.onChange?.invoke(index)
  }

  override fun body(): ViewBuilder {
    val context = this
    // 坑点: 避免成员变量直接引用导致双隐式receiver
    val _state = tabsState
    val _items = items
    val _config = config
    val barAtStart = _config.barPosition == BarPosition.START
    val _pageWidth = _config.pageWidth ?: pagerData.pageViewWidth
    return {
      View {
        attr {
          flex(1f)
          alignItems(FlexAlign.STRETCH)
        }

        // 顶部页签
        if (barAtStart) {
          tabsBar(_state, _items, _config, { context.changeIndex(it) })
          // 页签栏与内容之间的分割线
          tabsDivider(_config)
        }

        // Container
        PageList {
          ref { context.pageListRef = it }
          attr {
            flex(1f)
            pageDirection(true)
            pageItemWidth(_pageWidth)
            defaultPageIndex(context.controller.initialIndex)
            showScrollerIndicator(false)
            scrollEnable(_config.scrollable)
          }
          event {
            pageIndexDidChanged { raw ->
              val _JSONObject = raw as? JSONObject
              val index = _JSONObject?.optInt("index", _state.selected) ?: _state.selected
              if (index != _state.selected) {
                _state.selected = index
                context.controller.onChange?.invoke(index)
              }
            }
          }

          _items.forEachIndexed { _, item ->
            View {
              attr {
                width(_pageWidth)
              }
              item.content.invoke(this)
            }
          }
        }

        // 底部页签
        if (!barAtStart) {
          tabsDivider(_config)
          tabsBar(_state, _items, _config, { context.changeIndex(it) })
        }
      }
    }
  }
}

/** 页签栏 */
private fun ViewContainer<*, *>.tabsBar(
  state: TabsState,
  items: List<TabContentSpec>,
  config: TabsConfig,
  onClick: (Int) -> Unit
) {
  View {
    attr {
      flexDirectionRow()
      alignItems(FlexAlign.CENTER)
      height(config.barHeight)
      config.barWidth?.let { width(it) } // barWidth为空时 父容器适配整页宽度和父组件剩余宽度
      backgroundColor(config.barBackgroundColor)
      padding(0f, 16f, 0f, 16f)
    }
    if (config.tabBar != null) {
      // 自定义TabBar
      config.tabBar.invoke(this, TabBarParams(items, state, onClick, config.equalDivision, config.tabItemSpacing))
    } else {
      // 内置TabBar
      tabBarItems(state, items, config.equalDivision, onClick, config.tabItemSpacing)
    }
  }
}

/** 页签项 */
private fun ViewContainer<*, *>.tabBarItems(state: TabsState, items: List<TabContentSpec>, equal: Boolean, onClick: (Int) -> Unit, tabItemSpacing: Float = 0f) {
  View {
    attr {
      flex(1f)
      flexDirectionRow()
      alignItems(FlexAlign.CENTER)
    }

    items.forEachIndexed { index, item ->
      View {
        attr {
          alignItems(FlexAlign.CENTER)
          if (equal) {
            flex(1f)
          }
          if (tabItemSpacing > 0f && index < items.size - 1) {
            marginRight(tabItemSpacing)
          }
        }
        event {
          click {
            onClick(index)
          }
        }
        // 选中态变化时只重建item内部
        vbind({ state.selected }) {
          item.barBuilder?.invoke(this, state.selected == index)
        }
      }
    }
  }
}

/** 页签栏与内容之间的分割线 */
private fun ViewContainer<*, *>.tabsDivider(config: TabsConfig) {
  val divider = config.divider ?: return
  View {
    attr {
      height(divider.strokeWidth)
      backgroundColor(divider.color)
      margin(0f, divider.startMargin, 0f, divider.endMargin)
    }
  }
}


internal class TabsAttr: ComposeAttr() {}

internal class TabsEvent: ComposeEvent() {}

internal fun ViewContainer<*, *>.Tabs(
  controller: TabsController = TabsController(),
  barPosition: BarPosition = BarPosition.START,
  equalDivision: Boolean = true,
  tabItemSpacing: Float = 0f, // 页签项之间的间距
  barWidth: Float? = null,
  barHeight: Float = 56f,
  barBackgroundColor: Color = Color.WHITE,
  divider: DividerStyle? = null,
  scrollable: Boolean = true,
  animationDuration: Int = 300,
  pageWidth: Float? = null,
  tabBar: TabBarSlot? = null,
  content: TabsScope.() -> Unit
) {
  val scope = TabsScope()
  scope.content()
  val config = TabsConfig(barPosition, equalDivision, tabItemSpacing, barWidth, barHeight, barBackgroundColor, divider, scrollable, animationDuration, pageWidth, tabBar)
  // 选中态单一数据源
  val sharedState = controller.acquireState(this.pagerId, controller.initialIndex)
  val view = TabsView(controller, scope.items, config, sharedState)
  controller.barHost = view
  addChild(view) {
    attr {
      flex(1f)
    }
  }
}