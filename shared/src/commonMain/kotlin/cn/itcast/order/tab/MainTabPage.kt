package cn.itcast.order.tab

import cn.itcast.order.base.BasePager
import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewRef
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.utils.PlatformUtils
import com.tencent.kuikly.core.views.PageList
import com.tencent.kuikly.core.views.PageListView
import com.tencent.kuikly.core.views.View

/**
 * 底部 Tab 容器页：首页 / 下单 / 我的
 * 支持左右滑动切换页签，也支持点击底部 Tab 切换（通过 PageList#scrollToPageIndex）
 *
 * 参考文档：
 * - docs/API/components/page-list.md
 * - demo: demo/src/commonMain/kotlin/com/tencent/kuikly/demo/pages/app/AppTabPage.kt
 */
@Page("main_tab", supportInLocal = true)
internal class MainTabPage : BasePager() {

    private var selectedIndex: Int by observable(0)
    private var pageListRef: ViewRef<PageListView<*, *>>? = null

    override fun body(): ViewBuilder {
        val ctx = this
        val tabItems = listOf(
            TabItem("首页", TabIconType.HOME),
            TabItem("下单", TabIconType.ORDER),
            TabItem("我的", TabIconType.PROFILE)
        )
        val isGlassSupported = PlatformUtils.isIOS() && PlatformUtils.isLiquidGlassSupported()

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
                }
                event {
                    pageIndexDidChanged {
                        ctx.selectedIndex = (it as JSONObject).optInt("index")
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
                        left = TabTheme.TAB_BAR_MARGIN,
                        right = TabTheme.TAB_BAR_MARGIN
                    )
                    tabs = tabItems
                    selectedIndex = ctx.selectedIndex
                    glassEnable = isGlassSupported
                }
                event {
                    onTabSelected { index ->
                        ctx.selectedIndex = index
                        ctx.pageListRef?.view?.scrollToPageIndex(index, true)
                    }
                }
            }
        }
    }
}