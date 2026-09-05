package cn.itcast.order

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import cn.itcast.order.base.AppTheme
import cn.itcast.order.base.isSystemDark

class KRApplication : Application() {

  init {
    application = this
  }

  override fun onCreate() {
    super.onCreate()
    // 与当前系统主题对齐
    AppTheme.updateDark(isSystemDark())
    // 监听系统深浅色切换
    registerComponentCallbacks(object : ComponentCallbacks2 {
      override fun onConfigurationChanged(newConfig: Configuration) {
        val isNight = (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
          Configuration.UI_MODE_NIGHT_YES
        AppTheme.updateDark(isNight)
      }
      override fun onLowMemory() {}
      override fun onTrimMemory(level: Int) {}
    })
  }

  companion object {
    lateinit var application: Application
  }
}
