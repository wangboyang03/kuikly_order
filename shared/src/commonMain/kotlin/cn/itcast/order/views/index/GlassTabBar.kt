package cn.itcast.order.views.index

import cn.itcast.order.models.TabItemParams
import com.tencent.kuikly.core.base.Border
import com.tencent.kuikly.core.base.BorderStyle
import com.tencent.kuikly.core.base.BoxShadow
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.Translate
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.base.event.ClickParams
import com.tencent.kuikly.core.base.event.PanGestureParams
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.reactive.handler.observable
import com.tencent.kuikly.core.timer.clearTimeout
import com.tencent.kuikly.core.timer.setTimeout
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class Tabs : ComposeView<TabsOptions, TabsEvent>() {
  // kuikly的泛型机制 属性类型和事件类型
  // 在Kotlin中 泛型在运行期是擦除的 -> reified也只能用在inline函数里 所以父类把创建动作定义成抽象方法 子类负责实现
  override fun createAttr(): TabsOptions = TabsOptions()
  override fun createEvent(): TabsEvent = TabsEvent()
  override fun body(): ViewBuilder {
    val context = this // 因为Kotlin DSL中存在大量带接收者的lambda 在不同lambda表达式中 this指向会发生变化

    return {
      View {
        attr {
          height(TabTheme.TAB_BAR_HEIGHT)
          flexDirection(FlexDirection.ROW)
          alignItems(FlexAlign.CENTER)
          borderRadius(TabTheme.TAB_BAR_HEIGHT / 2f)
          backgroundColor(TabTheme.tabBarBackground)
          boxShadow(BoxShadow(0f, 4f, 12f, Color(0x0A000000)))
          touchEnable(true)
        }
        event {
          click { params: ClickParams ->
            val tappedIndex = (params.x / context.attr.itemWidth).toInt().coerceIn(0, context.attr.tabs.size - 1)
            context.animateLensTo(tappedIndex.toFloat())
            context.event.tabSelectHandler?.invoke(tappedIndex)
          }
          pan { params: PanGestureParams ->
            when (params.state) {
              "start" -> {
                context.cancelLensTween()
                context.attr.scrollProgress = (params.x / context.attr.itemWidth - 0.5f).coerceIn(
                  0f,
                  (context.attr.tabs.size - 1).toFloat()
                )
                val fingerSlot =
                  (params.x / context.attr.itemWidth).toInt().coerceIn(0, context.attr.tabs.size - 1)
                context.event.tabSelectHandler?.invoke(fingerSlot)
              }

              "move" -> {
                // 镜片中心始终落在手指正下方：progress = 手指x/槽宽 - 0.5
                val newProgress =
                  (params.x / context.attr.itemWidth - 0.5f).coerceIn(
                    0f,
                    (context.attr.tabs.size - 1).toFloat()
                  )
                // 直接更新连续进度：transform 响应式即时跟随，无动画、零延迟 = 完全跟手
                context.attr.scrollProgress = newProgress
              }

              "end" -> {
                // 松手吸附到最近的整页（过半则落到下一侧），并带一个小动画
                val snapped = (context.attr.scrollProgress + 0.5f).toInt()
                context.animateLensTo(snapped.toFloat())
                context.event.tabSelectHandler?.invoke(snapped)
              }
            }
          }
        }

        if (context.attr.itemWidth > 0f) {
          View {
            attr {
              absolutePosition(
                left = TabTheme.LENS_HORIZONTAL_INSET,
                top = TabTheme.LENS_VERTICAL_INSET
              )
              size(
                context.attr.itemWidth - 2f * TabTheme.LENS_HORIZONTAL_INSET,
                TabTheme.LENS_HEIGHT
              )
              borderRadius(TabTheme.LENS_HEIGHT / 2f)
              backgroundColor(TabTheme.lensBackground)
              border(Border(1f, BorderStyle.SOLID, TabTheme.lensBorderColor))
              // 连续偏移：scrollProgress * 每槽宽度（随 scrollProgress 变化即时重算）
              transform(
                Translate(
                  percentageX = 0f,
                  percentageY = 0f,
                  offsetX = context.attr.scrollProgress * context.attr.itemWidth
                )
              )
            }
          }
        }

        for (index in context.attr.tabs.indices) {
          View {
            attr {
              flex(1f)
              height(TabTheme.LENS_HEIGHT)
              allCenter()
            }
            // 图标 + 角标 容器：固定 22x22，角标用 absolutePosition 钉在右上角外侧
            View {
              attr {
                size(TabTheme.TAB_ICON_SIZE, TabTheme.TAB_ICON_SIZE)
              }
              Image {
                attr {
                  size(TabTheme.TAB_ICON_SIZE, TabTheme.TAB_ICON_SIZE)
                  // 各图标 viewBox 宽高比不同（如首页为 64.86:50），必须 contain 否则会被拉伸变形
                  resizeContain()
                  src(
                    ImageUri.commonAssets(
                      context.attr.tabs[index].icon(
                        selected = index == context.attr.selectedIndex,
                        orderedToday = context.attr.orderedToday
                      )
                    )
                  )
                }
              }
              vif({ context.attr.tabs[index].badge != null }) {
                View {
                  attr {
                    absolutePosition(top = -3f, right = -3f)
                    size(16f, 16f)
                    borderRadius(8f)
                    backgroundColor(TabTheme.badgeBackground)
                    allCenter()
                  }
                  Text {
                    attr {
                      text(context.attr.tabs[index].badge ?: "")
                      fontSize(9f)
                      fontWeight700()
                      color(Color.WHITE)
                    }
                  }
                }
              }
            }
            Text {
              attr {
                marginTop(2f)
                text(context.attr.tabs[index].name)
                fontSize(11f)
                fontWeight600()
                color(
                  if (index == context.attr.selectedIndex) {
                    TabTheme.tabTextActivated
                  } else {
                    TabTheme.tabTextNormal
                  }
                )
              }
            }
          }
        }
      }
    }
  }

  // ---- 镜片吸附动画：拖拽中直接跟手（无动画），松手/点选时平滑滑到目标 tab ----
  private fun cancelLensTween() {
    if (attr.tweenTimerId.isNotEmpty()) {
      clearTimeout(attr.tweenTimerId)
      attr.tweenTimerId = ""
    }
  }

  /** 让镜片从当前位置平滑滑到 target（拖拽结束 / 点选时调用） */
  private fun animateLensTo(target: Float) {
    cancelLensTween()
    attr.animFrom = attr.scrollProgress
    attr.animTo = target.coerceIn(0f, (attr.tabs.size - 1).toFloat())
    attr.animTotalFrames = (attr.animDurationMs / attr.animFrameMs).toInt().coerceAtLeast(1)
    attr.animFrame = 0
    stepLensTween()
  }

  private fun stepLensTween() {
    attr.animFrame += 1
    val t = (attr.animFrame.toFloat() / attr.animTotalFrames).coerceIn(0f, 1f)
    // easeOutCubic：起步快、收尾缓，吸附手感自然
    val eased = 1f - (1f - t) * (1f - t) * (1f - t)
    attr.scrollProgress = attr.animFrom + (attr.animTo - attr.animFrom) * eased
    if (attr.animFrame < attr.animTotalFrames) {
      attr.tweenTimerId = setTimeout(attr.animFrameMs.toInt()) { stepLensTween() }
    } else {
      attr.scrollProgress = attr.animTo
      attr.tweenTimerId = ""
    }
  }

}

internal class TabsOptions : ComposeAttr() {
  /** Tab 数据，静态配置，无需响应式 */
  var tabs: List<TabItemParams> = emptyList()

  /** 离散选中索引：控制文字/图标颜色（拖拽过程中不变色，避免闪烁） */
  var selectedIndex: Int by observable(0)

  /**
   * 连续滚动进度：控制镜片 X 轴平移。
   * - 0.0 = 第 1 个 Tab 居中
   * - 1.0 = 第 2 个 Tab 居中
   * - 2.0 = 第 3 个 Tab 居中
   * - 中间值（如 0.5）= 在两个 Tab 之间（拖拽跟手时使用）
   *
   * 直接绑定到 transform.offsetX，响应式即时更新，无动画无延迟。
   */
  var scrollProgress: Float by observable(0f)

  /** 今日是否已下单，决定 ORDER tab 选中态用 order_full 还是 order_empty */
  var orderedToday: Boolean by observable(false)

  /** 单个 Tab 槽位的宽度（dp），由父组件按屏幕宽度计算后传入；用于镜片滑动距离换算 */
  var itemWidth: Float = 0f
  // ▼ 吸附动画状态（非响应式，仅动画过程使用）
  /** 进行中的动画定时器 id，空串表示无动画 */
  var tweenTimerId: String = ""

  /** 动画起点 progress */
  var animFrom: Float = 0f

  /** 动画终点 progress */
  var animTo: Float = 0f

  /** 吸附动画总帧数（由时长 / 帧间隔换算，避免依赖系统时钟） */
  var animTotalFrames: Int = 14

  /** 当前已播放帧数 */
  var animFrame: Int = 0

  /** 吸附动画时长（毫秒） */
  var animDurationMs: Long = 220L

  /** 动画帧间隔（毫秒） */
  var animFrameMs: Long = 16L
}

internal class TabsEvent : ComposeEvent() {
  var tabSelectHandler: ((Int) -> Unit)? = null

  fun onTabSelected(handler: (Int) -> Unit) {
    tabSelectHandler = handler
  }
}

internal fun ViewContainer<*, *>.Tabs(init: Tabs.() -> Unit) {
  addChild(Tabs(), init)
}
