package io.homeassistant.android.onboarding


import android.os.Bundle
import android.view.View
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
            findNavController().navigate(OnboardingFragmentDirections.toScanningFragment())
        }
    }

    //TODO hide or show wifi warning depending on network status
}
