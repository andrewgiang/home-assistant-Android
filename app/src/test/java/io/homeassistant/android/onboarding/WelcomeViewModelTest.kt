package io.homeassistant.android.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.homeassistant.android.R
import io.homeassistant.android.core.NavigationEvent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class WelcomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var subject: WelcomeViewModel

    @Before
    fun setUp() {
        subject = WelcomeViewModel()
    }

    @Test
    fun `will navigate to scanning fragment if wifi is connected`() {
        subject.setIsWifiConnected(true)
        subject.onContinueClick()

        assertEquals(
            NavigationEvent.NavByDirection(WelcomeFragmentDirections.toScanningFragment()),
            subject.navigation.value?.peekContent()
        )
    }

    @Test
    fun `will navigate to manual configuration fragment if wifi is not connected`() {
        subject.setIsWifiConnected(false)
        subject.onContinueClick()

        assertEquals(
            NavigationEvent.NavById(R.id.manualConfigurationFragment),
            subject.navigation.value?.peekContent()
        )
    }


}