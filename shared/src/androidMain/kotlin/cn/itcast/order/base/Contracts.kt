package cn.itcast.order.base

import android.content.res.Configuration
import android.content.res.Resources

actual fun isSystemDark(): Boolean {
  val currentColorMode = Resources.getSystem().configuration.uiMode
  return (currentColorMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

actual fun observeSystemThemeChanges(onChange: (Boolean) -> Unit) {}