package cn.itcast.kuikly_order.views.components

import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ContainerAttr
import com.tencent.kuikly.core.base.ViewConst
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.event.Event

internal class BindContentCover: ViewContainer<BindContentCoverAttr, BindContentCoverEvent>() {
  override fun createEvent(): BindContentCoverEvent {
    return BindContentCoverEvent()
  }

  override fun createAttr(): BindContentCoverAttr {
    return BindContentCoverAttr()
  }

  override fun viewName(): String {
    return ViewConst.TYPE_VIEW
  }
}


internal class BindContentCoverAttr: ContainerAttr() {}

internal class BindContentCoverEvent: Event() {}

internal fun ViewContainer<*, *>.BindContentCover(init: BindContentCover.() -> Unit) {
  addChild(BindContentCover()) {
    attr {
      backgroundColor(Color.WHITE)
      borderRadius(18f, 18f, 0f, 0f)
    }
    init()
  }
}
