package io.homeassistant.android


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.homeassistant.android.AuthViewModel.*

class MainFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            authState->
            when(authState){
                AuthState.AUTHENTICATED -> {showWebView()}
                AuthState.UNAUTHENTICATED ->{ navController.navigate(R.id.onboarding)}
            }
        })

    }

    private fun showWebView() {
        view?.findViewById<WebView>(R.id.webview)?.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://demo.home-assistant.io")
        }
    }
}
