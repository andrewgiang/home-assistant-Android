package io.homeassistant.android.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.homeassistant.android.R
import kotlinx.android.synthetic.main.fragment_discovered_instances.*

class DiscoveredInstancesFragment : Fragment() {

    private val args by navArgs<DiscoveredInstancesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discovered_instances, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foundInstances.text = getString(R.string.found_instances, args.instances.size)
    }
}
