package io.homeassistant.android.onboarding


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import io.homeassistant.android.R
import io.homeassistant.android.core.observeNavigationEvents
import io.homeassistant.android.injector
import kotlinx.android.synthetic.main.fragment_welcome.*
import javax.inject.Inject

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private val viewModel: WelcomeViewModel by activityViewModels()

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigation.observeNavigationEvents(viewLifecycleOwner, findNavController())
        viewModel.ui.observe(viewLifecycleOwner) { uiState ->
            if (uiState.startAnimation) {
                animationView.setMaxFrame("Circles Formed")
                animationView.playAnimation()
            }
            showWifiMessage(!uiState.isWifiConnected)
        }

        continueBtn.setOnClickListener {
            viewModel.onContinueClick()
        }

        listenForNetworkChange()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.let { networkInfo ->
                viewModel.setIsWifiConnected(networkInfo.isConnected)
            }
        }

    }

    private fun showWifiMessage(shouldShow: Boolean) {
        wifiMessage.animate().alpha(if (shouldShow) 1f else 0f).withEndAction {
            wifiMessage.visibility = if (shouldShow) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun listenForNetworkChange() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(
                broadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        super.onAvailable(network)
                        viewModel.setIsWifiConnected(true)
                    }

                    override fun onLost(network: Network?) {
                        super.onLost(network)
                        viewModel.setIsWifiConnected(false)
                    }
                }, Handler(Looper.getMainLooper())
            )
        }
    }
}
