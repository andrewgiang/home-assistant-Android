package io.homeassistant.android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    enum class AuthState {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    val authenticationState = MutableLiveData<AuthState>().apply {
        value = AuthState.UNAUTHENTICATED
    }

}
