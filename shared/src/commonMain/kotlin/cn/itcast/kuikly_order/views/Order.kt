package cn.itcast.kuikly_order.views

import cn.itcast.kuikly_order.viewmodels.OrderViewModel
import cn.itcast.kuikly_order.views.components.BindContentCover
import cn.itcast.kuikly_order.views.components.CategorySidebar
import cn.itcast.kuikly_order.views.components.DividerStyle
import cn.itcast.kuikly_order.views.components.IndicatorStyle
import cn.itcast.kuikly_order.views.components.PromptCard
import cn.itcast.kuikly_order.views.components.Tabs as ComponentTabs
import cn.itcast.kuikly_order.views.components.TabsController
import cn.itcast.kuikly_order.views.components.TabsScope
import com.tencent.kuikly.core.base.ComposeView
import com.tencent.kuikly.core.base.ComposeAttr
import com.tencent.kuikly.core.base.ComposeEvent
import com.tencent.kuikly.core.base.ViewBuilder
import com.tencent.kuikly.core.base.ViewContainer
import com.tencent.kuikly.core.base.Color
import com.tencent.kuikly.core.base.attr.ImageUri
import com.tencent.kuikly.core.layout.FlexAlign
import com.tencent.kuikly.core.layout.FlexDirection
import com.tencent.kuikly.core.layout.FlexJustifyContent
import com.tencent.kuikly.core.views.Image
import com.tencent.kuikly.core.views.Text
import com.tencent.kuikly.core.views.View

internal class OrderView: ComposeView<OrderViewAttr, OrderViewEvent>() {
  private val orderViewModel: OrderViewModel by lazy(LazyThreadSafetyMode.NONE) {
    OrderViewModel(pagerId)
  }

  override fun createEvent(): OrderViewEvent {
    return OrderViewEvent()
  }

  override fun createAttr(): OrderViewAttr {
    return OrderViewAttr()
  }

  override fun body(): ViewBuilder {
    val context = this
    val imageHeight = context.pagerData.pageViewWidth / 3f
    return {
      View {
        attr {
          flex(1f)
          backgroundColor(Color.WHITE)
        }
        // 顶部图片
        View {
          attr {
            absolutePosition(0f, 0f, right = 0f)
            height(imageHeight + 18f)
            backgroundColor(0xFFCCCCBDL)
          }
        }
        // 内容区
        BindContentCover {
          attr {
            marginTop(imageHeight)
            flex(1f)
          }

          // header
          View {
            attr {
              padding(12f, 16f, bottom = 8f, right = 16f)
            }

            View {
              attr {
                flexDirection(FlexDirection.ROW)
                alignItems(FlexAlign.CENTER)
              }

              AvatarView().invoke(this)

              View {
                attr {
                  flex(1f)
                  marginLeft(12f)
                }

                View {
                  attr {
                    flexDirection(FlexDirection.ROW)
                    alignItems(FlexAlign.CENTER)
                  }

                  LevelView(level = 0).invoke(this)

                  // 标题
                  Text {
                    attr {
                      text("默认标题")
                      fontSize(18f)
                      color(0xFF333333L)
                      fontWeightSemiBold()
                      marginLeft(8f)
                    }
                  }
                }

                // 人数
                Text {
                  attr {
                    text("默认人数")
                    fontSize(13f)
                    color(0xFFA5A5A5L)
                    marginTop(6f)
                  }
                }
              }
            }

            AnnouncementView("暂无公告").invoke(this)
          }

          val orderTabsController = TabsController(context.orderViewModel.selectedTabIndex).apply {
            onChange = { index ->
              context.orderViewModel.changeTabItem(index)
            }
          }

          val orderTabs: TabsScope.() -> Unit = {
            TabContent {
              View {
                attr {
                  flexDirection(FlexDirection.ROW)
                  flex(1f)
                }

                CategorySidebar {}

                View {
                  attr {
                    flex(1f)
                    justifyContent(FlexJustifyContent.CENTER)
                    alignItems(FlexAlign.CENTER)
                    padding(8f, 20f, 48f, 20f)
                  }

                  PromptCard {
                    attr {
                      imageSrc(ImageUri.commonAssets("system_icon_empty.png"))
                      mainLabel("无内容")
                      subLabel("点击下方按钮添加内容")
                      buttonLabel("现在添加")
                    }
                  }
                }
              }
            }.tabBar("点单", IndicatorStyle())

            TabContent {
              View {
                attr {
                  flex(1f)
                }
              }
            }.tabBar("修改", IndicatorStyle())
          }

          // 二级页签 + 内容：单个 Tabs，自定义 tabBar（内置页签项 + 搜索框），对齐 ArkUI 做法
          ComponentTabs(
            controller = orderTabsController,
            equalDivision = false,
            tabItemSpacing = 18f,
            barHeight = 48f,
            barBackgroundColor = Color.WHITE,
            scrollable = false,
            divider = DividerStyle(strokeWidth = 1f, color = Color(0xFFF0F0F0L)),
            tabBar = { params ->
              params.renderItems(this)
              SearchView().invoke(this)
            }
          ) {
            orderTabs()
          }
        }
      }
    }
  }
}

/** 头像 */
private fun AvatarView(): ViewBuilder {
  return {
    View {
      attr {
        size(52f, 52f)
        backgroundColor(0xFFF4F2F7L)
        borderRadius(10f)
        alignItems(FlexAlign.CENTER)
        justifyContent(FlexJustifyContent.CENTER)
      }
      Image {
        attr {
          size(74f, 74f)
          src(ImageUri.commonAssets("order_image_avatar.png"))
          resizeCover()
        }
      }
    }
  }
}

/** 等级 */
private fun LevelView(level: Int): ViewBuilder {
  return {
    View {
      attr {
        backgroundColor(0xFF8E7DAEL)
        borderRadius(4f)
        padding(2f, 6f, 2f, 6f)
      }

      Text {
        attr {
          text("Lv.$level")
          fontSize(12f)
          color(Color.WHITE)
        }
      }
    }
  }
}

/** 公告 */
private fun AnnouncementView(notice: String): ViewBuilder {
  return {
    Text {
      attr {
        text("公告：$notice")
        fontSize(14f)
        color(0xFFA5A5A5L)
        marginTop(10f)
      }
    }
  }
}

/** 搜索 */
private fun SearchView(): ViewBuilder {
  return {
    View {
      attr {
        flexDirection(FlexDirection.ROW)
        alignItems(FlexAlign.CENTER)
        justifyContent(FlexJustifyContent.CENTER)
        width(100f)
        height(34f)
        padding(0f, 14f, 0f, 14f)
        backgroundColor(Color(0xFFF1F1F1L))
        borderRadius(17f)
      }

      Image {
        attr {
          size(16f, 16f)
          resizeContain()
          src(ImageUri.commonAssets("common_icon_search.png"))
        }
      }

      Text {
        attr {
          text("搜索")
          fontSize(14f)
          color(Color(0xFF666666L))
          marginLeft(6f)
        }
      }
    }
  }
}


internal class OrderViewAttr: ComposeAttr() {}

internal class OrderViewEvent: ComposeEvent() {}

internal fun ViewContainer<*, *>.order(init: OrderView.() -> Unit) {
  addChild(OrderView()) {
    attr {
      flex(1f)
    }
    init()
  }
}