package cn.itcast.order.tab

import cn.itcast.order.base.BasePager
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.PageListView
import com.tencent.kuikly.core.views.View

@Page("main_tab", supportInLocal = true)
internal class MainTabPage : BasePager() {
    private var selectedIndex: Int by observable(0)
    private var pageListRef: ViewRef<PageListView<*, *>>? = null
    private var pendingPageIndex: Int = -1

    /**
     * 今日是否已下单：决定「下单」tab 选中态用 order_full 还是 order_empty。
     * TODO: 接入真实数据源（如下单成功后的本地标记 / 接口返回），目前默认 false
     */
    private var orderedToday: Boolean by observable(false)

    override fun body(): ViewBuilder {
        val ctx = this
        val tabItems = listOf(
            TabItem("首页", TabIconType.HOME, badge = "7"),
            TabItem("下单", TabIconType.ORDER, badge = "1"),
            TabItem("我的", TabIconType.PROFILE)
        )
        // 单个 Tab 槽位宽度：栏内宽 / 槽数，传给 GlassTabBar 用于滑动镜片距离换算
        // 栏内宽封顶 TAB_BAR_MAX_WIDTH 并居中，避免大屏上整体拉得太长
        val pageWidth = pagerData.pageViewWidth
        val tabBarInnerWidth = (pageWidth - 2f * TabTheme.TAB_BAR_MARGIN)
            .coerceAtMost(TabTheme.TAB_BAR_MAX_WIDTH)
        val tabBarSideMargin = (pageWidth - tabBarInnerWidth) / 2f
        val tabItemWidth = tabBarInnerWidth / tabItems.size

        return {
            attr {
                backgroundColor(TabTheme.pageBackground)
            }

            // 状态栏占位
            View {
                attr {
                    height(pagerData.statusBarHeight)
                    backgroundColor(TabTheme.pageBackground)
                }
            }

            PageList {
                ref {
                    ctx.pageListRef = it
                }
                attr {
                    flexDirectionRow()
                    pageDirection(true)
                    pageItemWidth(pagerData.pageViewWidth)
                    pageItemHeight(pagerData.pageViewHeight - pagerData.statusBarHeight)
                    defaultPageIndex(0)
                    firstContentLoadMaxIndex(tabItems.size)
                    showScrollerIndicator(false)
                    keepItemAlive(true)
                    // 禁止用户在内容区左右滑动切换页面（由底 Tab 栏手势统一接管）
                    scrollEnable(false)
                    pagingEnable(false)
                }
                event {
                    // 页面索引变化（程序化翻页后回写选中态）
                    pageIndexDidChanged {
                        val index = (it as JSONObject).optInt("index")
                        // 程序化翻页过程中只接受最终目标页，忽略途经的中间页
                        if (ctx.pendingPageIndex >= 0 && index != ctx.pendingPageIndex) {
                            return@pageIndexDidChanged
                        }
                        ctx.pendingPageIndex = -1
                        ctx.selectedIndex = index
                    }
                }

                HomeTabView { }
                OrderTabView { }
                ProfileTabView { }
            }

            GlassTabBar {
                attr {
                    absolutePosition(
                        bottom = TabTheme.TAB_BAR_BOTTOM + pagerData.safeAreaInsets.bottom,
                        left = tabBarSideMargin,
                        right = tabBarSideMargin
                    )
                    tabs = tabItems
                    selectedIndex = ctx.selectedIndex
                    orderedToday = ctx.orderedToday
                    itemWidth = tabItemWidth
                }
                event {
                    onTabSelected { index ->
                        if (index == ctx.selectedIndex) return@onTabSelected
                        ctx.pendingPageIndex = index
                        // 更新选中态并驱动 PageList 翻页（内容直接切换，镜片由 GlassTabBar 自行平滑吸附）
                        ctx.selectedIndex = index
                        ctx.pageListRef?.view?.scrollToPageIndex(index, false)
                    }
                }
            }
        }
    }
}
