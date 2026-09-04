import com.tencent.kuikly.core.render.web.collection.FastMutableMap
import com.tencent.kuikly.core.render.web.ktx.SizeI
import com.tencent.kuikly.core.render.web.runtime.miniapp.MiniDocument
import com.tencent.kuikly.core.render.web.runtime.miniapp.core.App
import com.tencent.kuikly.core.render.web.runtime.miniapp.core.NativeApi

const val TAG = "Main"

private const val PARAM_SAFE_AREA_BOTTOM = "safeAreaBottom"

/**
 * 宿主实现底部安全区避让
 * @param systemInfo 系统信息
 * @return 安全区避让值
 */
private fun bottomSafeAreaInset(systemInfo: dynamic): Double {
  if (systemInfo == null) return 0.0
  // 宿主相对屏幕的安全区
  val safeArea: dynamic = systemInfo.safeArea ?: return 0.0
  // 宿主屏幕高度
  val screenHeight: dynamic = systemInfo.screenHeight
  // 宿主底部安全区高度起始值 安全区底边Y坐标
  val safeAreaBottom: dynamic = safeArea.bottom
  if (screenHeight == null || safeAreaBottom == null) return 0.0
  // 通过屏幕高度减去底部安全区起始值得到需要避让的高度
  val inset = screenHeight.unsafeCast<Double>() - safeAreaBottom.unsafeCast<Double>()
  return if (inset > 0.0) inset else 0.0
}

fun main() { // 小程序应用级生命周期
  App.onShow {
    console.log(TAG, "app show")
  }

  App.onLaunch {
    console.log(TAG, "app launch")
  }

  App.onHide {
    console.log(TAG, "app hide")
  }
}

/**
 *  Mini program page entry, use renderView delegate method to initialize and create renderView
 */
@JsName(name = "renderView") @JsExport @ExperimentalJsExport fun renderView(json: dynamic) {
  // Write to global render function
  val renderParams = FastMutableMap<String, dynamic>(json)
  // View size 由渲染层自行按窗口尺寸兜底
  var size: SizeI? = null
  if (json.width != null && json.height != null) {
    size = SizeI(json.width.unsafeCast<Int>(), json.height.unsafeCast<Int>()) // 只在宿主显式传了width/height时才构造
  }

  /**
   * 内部构造 pageOnLoadOptions + safeAreaInsets + is_miniprogram
   * usedParams = { platform: "miniprogram", statusBarHeight: ..., param: paramsMap }
   * onLoadCallback(pageId, pageName, usedParams)
   */
  MiniDocument.initPage(renderParams) { pageId: Int, pageName: String, paramsMap: FastMutableMap<String, Any> ->
    val systemInfo = NativeApi.plat.getSystemInfoSync()
    val isAndroid = systemInfo.platform == "android" // 读取宿主契约
    val params = paramsMap["param"].unsafeCast<FastMutableMap<String, Any>>()
    params["is_wx_mp"] = "true"
    // 小程序渲染层下发的safeAreaInsets只有顶部安全区 底部安全区为0 因此这里填充真实底部安全区避让
    params[PARAM_SAFE_AREA_BOTTOM] = bottomSafeAreaInset(systemInfo).toString()

    paramsMap["platform"] = if (isAndroid) "android" else "iOS" // Kuikly契约
    paramsMap["isIOS"] = !isAndroid
    paramsMap["isIphoneX"] = !isAndroid && systemInfo.safeArea.top > 30

    KuiklyWebRenderViewDelegator().delegate.onAttach(pageId, pageName, paramsMap, size,)
  }
}

/**
 * Register callback methods on the mini program App object, needs to be called in the app.js of the mini program
 */
@JsName(name = "initApp")
@JsExport
@ExperimentalJsExport
fun initApp(options: dynamic = js("{}")) {
  App.initApp(options)
}