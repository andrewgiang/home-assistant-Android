package io.homeassistant.android.inject

import android.content.Context
import android.net.nsd.NsdManager
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
    fun nsdManager() : NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

}