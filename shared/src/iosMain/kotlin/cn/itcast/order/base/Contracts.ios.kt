package cn.itcast.order.base

actual fun isSystemDark(): Boolean {
  // 只是占位 不实现
  return false
}

actual fun observeSystemThemeChanges(onChange: (Boolean) -> Unit) {}