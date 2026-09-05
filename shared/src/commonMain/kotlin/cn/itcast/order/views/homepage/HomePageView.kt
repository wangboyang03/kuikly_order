package cn.itcast.order.views.homepage

import cn.itcast.order.base.AppTheme
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class HomePageView: ComposeView<ComposeAttr, ComposeEvent>() {
  override fun createAttr(): ComposeAttr = ComposeAttr()
  override fun createEvent(): ComposeEvent = ComposeEvent()
  override fun body(): ViewBuilder {
    val context = this

    return {
      View {
        attr {
          flex(1f)
          allCenter()
          paddingTop(context.pagerData.statusBarHeight) // 开启沉浸式状态栏
          backgroundColor(AppTheme.background)
        }
      }
    }
  }
}

internal fun ViewContainer<*, *>.homepageView(init: HomePageView.() -> Unit) {
  addChild(HomePageView(), init)
}