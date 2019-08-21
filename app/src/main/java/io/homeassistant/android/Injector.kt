package io.homeassistant.android

import io.homeassistant.android.inject.AppComponent

fun injector(): AppComponent {
    return App.get().component
}