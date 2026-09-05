package cn.itcast.order.base

// 定义契约 由各平台自行实现
expect fun isSystemDark(): Boolean  // 系统是否为暗色模式

expect fun observeSystemThemeChanges(onChange: (Boolean) -> Unit) // 订阅宿主深浅色主题变化