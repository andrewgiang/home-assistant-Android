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
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.homeassistant.android.R
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationView.setMaxFrame("Circles Formed")
        animationView.playAnimation()

        continueBtn.setOnClickListener {
            if (wifiMessage.visibility != View.VISIBLE) {
                findNavController().navigate(OnboardingFragmentDirections.toScanningFragment())
            } else {
                findNavController().navigate(R.id.manualConfigurationFragment)
            }
        }

        listenForNetworkChange()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val connectivityManager = requireContext().getSystemService<ConnectivityManager>()
            connectivityManager?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.let { networkInfo ->
                showWifiMessage(!networkInfo.isConnected)
            }
        }

    }

    private fun showWifiMessage(shouldShow: Boolean) {

        wifiMessage.animate().alpha(if (shouldShow) 1f else 0f).withEndAction {
            wifiMessage.visibility = if (shouldShow) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun listenForNetworkChange() {
        val connectivityManager = requireContext().getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(
                broadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager?.registerNetworkCallback(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        super.onAvailable(network)
                        showWifiMessage(false)
                    }

                    override fun onLost(network: Network?) {
                        super.onLost(network)
                        showWifiMessage(true)
                    }
                }, Handler(Looper.getMainLooper())
            )
        }
    }
}
