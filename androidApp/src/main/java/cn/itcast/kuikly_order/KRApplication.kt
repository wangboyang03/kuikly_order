package cn.itcast.kuikly_order

import android.app.Application

class KRApplication : Application() {

  init {
    application = this
  }

  companion object {
    lateinit var application: Application
  }
}