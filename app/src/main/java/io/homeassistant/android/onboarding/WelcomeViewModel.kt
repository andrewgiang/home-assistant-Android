package io.homeassistant.android.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.homeassistant.android.R
import io.homeassistant.android.core.Event
import io.homeassistant.android.core.NavigationEvent

class WelcomeViewModel : ViewModel() {
    private val _state = MutableLiveData<UiState>().apply { value = UiState(startAnimation = true) }
    private val _navigation = MutableLiveData<Event<NavigationEvent>>()

    val ui: LiveData<UiState> = _state
    val navigation: LiveData<Event<NavigationEvent>> = _navigation

    fun setIsWifiConnected(connected: Boolean) {
        _state.postValue(UiState(isWifiConnected = connected))
    }

    fun onContinueClick() {
        if (_state.value?.isWifiConnected == true) {
            _navigation.postValue(Event(NavigationEvent.NavByDirection(WelcomeFragmentDirections.toScanningFragment())))
        } else {
            _navigation.postValue(Event(NavigationEvent.NavById(R.id.manualConfigurationFragment)))
        }
    }


}

data class UiState(val startAnimation: Boolean = false, val isWifiConnected: Boolean = false)
