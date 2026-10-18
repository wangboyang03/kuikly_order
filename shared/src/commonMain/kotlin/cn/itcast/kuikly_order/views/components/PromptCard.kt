package cn.itcast.kuikly_order.views.components

import cn.itcast.kuikly_order.base.Constants
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.base.event.EventName
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class PromptCardView: ComposeView<PromptCardAttr, PromptCardEvent>() {
  override fun createAttr(): PromptCardAttr {
    return PromptCardAttr()
  }

  override fun createEvent(): PromptCardEvent {
    return PromptCardEvent()
  }

  override fun body(): ViewBuilder {
    val context = this
    return {
      View {
        attr {
          backgroundColor(Color.WHITE)
          alignItems(FlexAlign.CENTER)
          padding(20f, 16f, 20f, 16f)
        }

        vif({ context.getViewAttr().imageSrc != null }) {
          Image {
            attr {
              size(160f, 128f)
              resizeContain()
              context.getViewAttr().imageSrc?.let {
                src(it)
              }
            }
          }
        }

        View {
          attr {
            alignItems(FlexAlign.CENTER)
            margin(12f, 0f, 0f, 0f)
          }

          Text {
            attr {
              text(context.getViewAttr().mainLabel)
              fontSize(16f)
              color(0xFF4A4A4AL)
            }
          }

          vif({ context.getViewAttr().subLabel.isNotEmpty() }) {
            Text {
              attr {
                text(context.getViewAttr().subLabel)
                fontSize(13f)
                color(0xFF8A8A8AL)
                margin(6f, 0f, 0f, 0f)
              }
            }
          }
        }

        vif({ context.getViewAttr().buttonLabel.isNotEmpty() }) {
          View {
            attr {
              size(200f, 44f)
              allCenter()
              backgroundColor(Constants.MAIN_COLOR)
              borderRadius(22f)
              boxShadow(BoxShadow(0f, 4f, 8f, Color(0x55FEB6C1)), true)
              margin(16f, 0f, 0f, 0f)
            }

            event {
              click {
                context.getViewEvent().handlerWithEventName(EventName.CLICK.value)?.invoke(it)
              }
            }

            Text {
              attr {
                text(context.getViewAttr().buttonLabel)
                fontSize(15f)
                color(Color.WHITE)
                fontWeightMedium()
              }
            }
          }
        }
      }
    }
  }
}

internal class PromptCardAttr: ComposeAttr() {
  var imageSrc: ImageUri? = null
  var mainLabel: String = ""
  var subLabel: String = ""
  var buttonLabel: String = ""

  fun imageSrc(imageSrc: ImageUri): PromptCardAttr {
    this.imageSrc = imageSrc
    return this
  }

  fun mainLabel(mainLabel: String): PromptCardAttr {
    this.mainLabel = mainLabel
    return this
  }

  fun subLabel(subLabel: String): PromptCardAttr {
    this.subLabel = subLabel
    return this
  }

  fun buttonLabel(buttonLabel: String): PromptCardAttr {
    this.buttonLabel = buttonLabel
    return this
  }
}

internal class PromptCardEvent: ComposeEvent() {}

internal fun ViewContainer<*, *>.PromptCard(init: PromptCardView.() -> Unit = {}) {
  addChild(PromptCardView(), init)
}