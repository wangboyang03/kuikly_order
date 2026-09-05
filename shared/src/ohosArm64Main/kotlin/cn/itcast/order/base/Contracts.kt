package cn.itcast.order.base

import com.tencent.kuikly.core.manager.PagerManager

actual fun isSystemDark(): Boolean {
  return try {
    val pager = PagerManager.getCurrentPager()
    val module = pager.acquireModule(BridgeModule.MODULE_NAME)
    (module as? BridgeModule)?.getSystemDarkMode() ?: false
  } catch (e: Throwable) {
    false
  }
}

actual fun observeSystemThemeChanges(onChange: (Boolean) -> Unit) {
  try {
    val pager = PagerManager.getCurrentPager()
    val module = pager.acquireModule(BridgeModule.MODULE_NAME)
    (module as? BridgeModule)?.observeSystemDarkMode(onChange)
  } catch (e: Throwable) {}
}