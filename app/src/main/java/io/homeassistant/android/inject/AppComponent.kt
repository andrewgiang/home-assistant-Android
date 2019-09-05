package io.homeassistant.android.inject

import dagger.Component
import io.homeassistant.android.onboarding.ManualConfigurationFragment
import io.homeassistant.android.onboarding.ScanningFragment
import io.homeassistant.android.onboarding.WelcomeFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(scanningFragment: ScanningFragment)
    fun inject(fragment: ManualConfigurationFragment)
    fun inject(welcomeFragment: WelcomeFragment)
}