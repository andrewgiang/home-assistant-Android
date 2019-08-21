package io.homeassistant.android

import android.app.Application
import io.homeassistant.android.inject.AppComponent
import io.homeassistant.android.inject.AppModule
import io.homeassistant.android.inject.DaggerAppComponent

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        this.component = DaggerAppComponent.builder().appModule(AppModule(context = this)).build()
    }

    companion object {
        private var INSTANCE: App? = null
        @JvmStatic
        fun get(): App = INSTANCE!!
    }

}


