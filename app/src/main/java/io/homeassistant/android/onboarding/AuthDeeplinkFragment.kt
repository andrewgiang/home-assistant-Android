package io.homeassistant.android.onboarding


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.homeassistant.android.R

class AuthDeeplinkFragment : Fragment(R.layout.fragment_auth_deeplink) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAuthorizationCodeFromDeepLink()?.let { code ->
            Log.d(AuthenticationFragment.TAG, code)
            return@onViewCreated
        }
    }

    private fun getAuthorizationCodeFromDeepLink() = arguments?.getString("code")

}
