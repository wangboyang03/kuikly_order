package cn.itcast.order.base

import kotlin.js.js

actual fun isSystemDark(): Boolean {
  val initial = getInitialTheme()
  observeThemeChange()
  return initial
}

/**
 * 读取初始主题
 */
private fun getInitialTheme(): Boolean {
  return js(
    """
        (function () {
          try {
            if (typeof wx != 'undefined' && typeof wx.getSystemInfoSync == 'function') {
              return wx.getSystemInfoSync().theme == 'dark';
            }
          } catch (e) {}
          try {
            if (typeof window !== 'undefined' && window.matchMedia) {
              return window.matchMedia('(prefers-color-scheme: dark)').matches;
            }
          } catch (e) {}
          return false;
        })()
        """
  ) as Boolean
}

actual fun observeSystemThemeChanges(onChange: (Boolean) -> Unit) {}

// 订阅宿主/系统主题变化
private fun observeThemeChange() {
    try {
        val isWechat = js("typeof wx !== 'undefined' ? wx : null")
        if (isWechat != null) {
            val wx = isWechat.unsafeCast<dynamic>()
            if (wx.onThemeChange != null) {
                wx.onThemeChange { res: dynamic ->
                    try {
                       AppTheme.updateDark(res?.theme == "dark")
                    } catch (e: Throwable) {
                    }
                }
            }
        } else {
            val win = js("typeof window !== 'undefined' ? window : null")
            if (win != null) {
                val window = win.unsafeCast<dynamic>()
                if (window.matchMedia != null) {
                    val mq = window.matchMedia("(prefers-color-scheme: dark)")
                    val handler = { e: dynamic -> AppTheme.updateDark(e?.matches == true) }
                    if (mq.addEventListener != null) mq.addEventListener("change", handler)
                    else if (mq.addListener != null) mq.addListener(handler)
                }
            }
        }
    } catch (e: Throwable) {}
}