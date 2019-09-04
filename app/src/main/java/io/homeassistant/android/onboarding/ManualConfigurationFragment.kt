package io.homeassistant.android.onboarding


import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import io.homeassistant.android.R
import io.homeassistant.android.injector
import kotlinx.android.synthetic.main.fragment_manual_configuration.*
import javax.inject.Inject

class ManualConfigurationFragment : Fragment(R.layout.fragment_manual_configuration) {
    @Inject
    lateinit var inputMethodManager: InputMethodManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector().inject(this)
        inputMethodManager.showSoftInput(urlEditText, InputMethodManager.SHOW_IMPLICIT)
    }
}
