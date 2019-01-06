package com.minosai.oneclick.ui.credentials

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.ui.credentials.slider.SliderAdapter
import com.minosai.oneclick.ui.credentials.slider.SliderTimer
import kotlinx.android.synthetic.main.fragment_credentials.*
import java.util.*
import javax.inject.Inject

class CredentialsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var credentialsViewModel: CredentialsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credentialsViewModel = ViewModelProviders.of(this, viewModelFactory).get(CredentialsViewModel::class.java)

        val colors = arrayListOf(Color.RED, Color.GREEN, Color.BLUE)
        val colorNames = arrayListOf("Red", "Green", "Blue")

        val titles = arrayListOf("Multiple Accounts", "Quick tiles for Quickies", "Find more productivity")
        val subTitles = arrayListOf(
                "Add or remove accounts",
                "Effortless login via Notification drawer and home screen widget",
                "Limit usage using sleep timer and login privately using incognito mode"
        )

        viewpager_onboarding.adapter = SliderAdapter(requireContext(), titles, subTitles)
        indicator_onboarding.setupWithViewPager(viewpager_onboarding, true)

        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(activity, viewpager_onboarding, titles.size), 4000, 6000)

        button_cred_next.setOnClickListener {

            var isValid = true
            input_creds_display_name.error = null
            input_creds_username.error = null
            input_creds_password.error = null

            val displayName = input_creds_display_name.editText?.text.toString()
            val userName = input_creds_username.editText?.text.toString()
            val password = input_creds_password.editText?.text.toString()

            if (userName.isBlank() || userName.isEmpty()) {
                input_creds_username.error = "Username should not be empty"
                isValid = false
            }

            if (password.isBlank() || password.isEmpty()) {
                input_creds_password.error = "Password should not be empty"
                isValid = false
            }

            if (isValid) {
                with(credentialsViewModel) {
                    addAccount(userName, password, true)
                    changeFirstOpenBoolean()
                    saveDisplayName(displayName)
                }
                findNavController(it).popBackStack()
            }
        }

    }

}