package cn.itcast.order.views.index

import cn.itcast.order.base.BasePager
import cn.itcast.order.viewmodels.TabViewModel
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.PageListView

/**
 * 项目入口页：底部悬浮 Tab + 内容页。
 *
 * View 层只做两件事：
 * 1. 渲染 [TabViewModel] 暴露的状态
 * 2. 把用户交互（点击 / 拖拽 / 翻页）转发给 [TabViewModel]
 *
 * 数据从哪来、选中态怎么算，全部由 ViewModel 负责。
 */
@Page("main_tab", supportInLocal = true)
internal class MainTabPage : BasePager() {

    // ViewModel 绑定本页面 scope，保证其响应式状态变更能准确通知到本页面
    private val viewModel = TabViewModel(this)

    private var pageListRef: ViewRef<PageListView<*, *>>? = null

    /** 程序化翻页的目标页，用于过滤 PageList 途经的中间页 */
    private var pendingPageIndex: Int = -1

    override fun body(): ViewBuilder {
        val ctx = this
        val vm = ctx.viewModel

        // 单个 Tab 槽位宽度：栏内宽 / 槽数，传给 GlassTabBar 用于滑动镜片距离换算
        // 栏内宽封顶 TAB_BAR_MAX_WIDTH 并居中，避免大屏上整体拉得太长
        val pageWidth = pagerData.pageViewWidth
        val tabBarInnerWidth = (pageWidth - 2f * TabTheme.TAB_BAR_MARGIN).coerceAtMost(TabTheme.TAB_BAR_MAX_WIDTH)
        val tabBarSideMargin = (pageWidth - tabBarInnerWidth) / 2f
        val tabItemWidth = tabBarInnerWidth / vm.tabCount

        return {
            attr {
                backgroundColor(TabTheme.pageBackground)
            }

            PageList {
                ref {
                    ctx.pageListRef = it
                }
                attr {
                    flexDirectionRow()
                    pageDirection(true)
                    pageItemWidth(pagerData.pageViewWidth)
                    // 全屏高度：状态栏区域归各页面自持（背景通顶 + 内容下移），容器不代劳
                    pageItemHeight(pagerData.pageViewHeight)
                    defaultPageIndex(0)
                    firstContentLoadMaxIndex(vm.tabCount)
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
                        vm.syncPageIndex(index)
                    }
                }

                // 三个业务页各自的独立实现；顺序必须与 vm.state.tabList 一致
                HomePage { }
                OrderPage { }
                MinePage { }
            }

            GlassTabBar {
                attr {
                    absolutePosition(
                        bottom = TabTheme.TAB_BAR_BOTTOM + pagerData.safeAreaInsets.bottom,
                        left = tabBarSideMargin,
                        right = tabBarSideMargin
                    )
                    tabs = vm.state.tabList
                    selectedIndex = vm.state.selectedIndex
                    orderedToday = vm.state.orderedToday
                    itemWidth = tabItemWidth
                }
                event {
                    onTabSelected { index ->
                        // 选中态发生变化才翻页（ViewModel 负责判断是否重复选中）
                        if (!vm.selectTab(index)) return@onTabSelected
                        ctx.pendingPageIndex = index
                        // 内容直接切换，镜片由 GlassTabBar 自行平滑吸附
                        ctx.pageListRef?.view?.scrollToPageIndex(index, false)
                    }
                }
            }
        }
    }
}
