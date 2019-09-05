package io.homeassistant.android.inject

import android.content.Context
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.view.inputmethod.InputMethodManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun appContext(): Context = context

    @Singleton
    @Provides
    fun nsdManager(): NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    @Singleton
    @Provides
    fun inputManager(): InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    @Singleton
    @Provides
    fun connectivityManager(): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}