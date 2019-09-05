package io.homeassistant.android.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.homeassistant.android.R
import kotlinx.android.synthetic.main.fragment_authentication.*
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    companion object {
        val TAG = AuthenticationFragment::class.java.name
    }

    private val args by navArgs<AuthenticationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = AuthRepository(args.instance.baseUrl)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val instance = repository.testConnection()
                Log.d(TAG, instance.toString())
                connectGroup.visibility = View.VISIBLE
                connectButton.setOnClickListener {
                    startActivity(getAuthorizeIntent(instance.baseHttpUrl))
                }

            } catch (e: Exception) {

            }
        }

        restartButton.setOnClickListener {
            findNavController().popBackStack(R.id.onboarding, false)
        }
    }

    private fun getAuthorizeIntent(baseUrl: HttpUrl): Intent {
        val authorizeUrl = buildAuthorizeUrl(baseUrl)
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse(authorizeUrl.toString())
        )
    }

    private fun buildAuthorizeUrl(baseUrl: HttpUrl): HttpUrl {
        val redirectURI = "homeassistant://auth-callback"
        val clientID = "https://home-assistant.io/iOS"

        return baseUrl.newBuilder()
            .addPathSegment("auth")
            .addPathSegment("authorize")
            .addQueryParameter("client_id", clientID)
            .addQueryParameter("redirect_uri", redirectURI)
            .build()
    }
}

