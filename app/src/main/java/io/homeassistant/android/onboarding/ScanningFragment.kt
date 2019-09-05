package io.homeassistant.android.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.homeassistant.android.R
import io.homeassistant.android.core.NetworkDiscovery
import io.homeassistant.android.injector
import kotlinx.android.synthetic.main.fragment_scanning.*
import javax.inject.Inject

class ScanningFragment : Fragment(R.layout.fragment_scanning) {

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery

    init {
        lifecycleScope.launchWhenResumed {
            val services = networkDiscovery.start(5000)
            findNavController().navigate(
                ScanningFragmentDirections.toDiscoveredInstances(
                    services.toTypedArray()
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationView.setMinFrame("Circle Fill Begins")
        animationView.setMaxFrame("Deform Begins")
        animationView.playAnimation()
        enterManuallyButton.setOnClickListener {
            findNavController().navigate(ScanningFragmentDirections.toManualConfigurationFragment())
        }
    }
}
