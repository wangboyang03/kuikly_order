package cn.itcast.kuikly_order.views.components

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ContainerAttr
import com.tencent.kuikly.core.base.ViewConst
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.event.Event
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

private val SIDEBAR_BG = Color(0xFFF9F9F9L)
private val CATEGORY_SELECTED_COLOR = Color(0xFFFA6666L)

internal class CategorySidebarView: ViewContainer<CategorySidebarAttr, CategorySidebarEvent>() {
  override fun createAttr(): CategorySidebarAttr {
    return CategorySidebarAttr()
  }

  override fun createEvent(): CategorySidebarEvent {
    return CategorySidebarEvent()
  }

  override fun viewName(): String {
    return ViewConst.TYPE_VIEW
  }
}


internal class CategorySidebarAttr: ContainerAttr() {}

internal class CategorySidebarEvent: Event() {}

internal fun ViewContainer<*, *>.CategorySidebar(init: CategorySidebarView.() -> Unit = {}) {
  addChild(CategorySidebarView()) {
    attr {
      width(100f)
      backgroundColor(SIDEBAR_BG)
    }

    View {
      attr {
        height(48f)
        allCenter()
      }

      Text {
        attr {
          text("默认分类")
          fontSize(14f)
          color(CATEGORY_SELECTED_COLOR)
          fontWeightMedium()
        }
      }
    }
    init()
  }
}