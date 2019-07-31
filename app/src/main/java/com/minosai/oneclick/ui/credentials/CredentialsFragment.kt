package com.minosai.oneclick.ui.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import kotlinx.android.synthetic.main.fragment_credentials.*
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

        button_cred_next.setOnClickListener {

            var isValid = true
            input_creds_display_name.error = null
            input_creds_username.error = null
            input_creds_password.error = null

            val displayName = input_creds_display_name.editText?.text.toString()
            val userName = input_creds_username.editText?.text.toString()
            val password = input_creds_password.editText?.text.toString()

            if (userName.isBlank() || userName.isEmpty()) {
                input_creds_username.error = "Username cannot be blank"
                isValid = false
            }

            if (password.isBlank() || password.isEmpty()) {
                input_creds_password.error = "Password cannot be blank"
                isValid = false
            }

            if (isValid) {
                showDialog(userName, password, displayName)
            }
        }

    }

    private fun showDialog(userName: String, password: String, displayName: String) {
        AlertDialog.Builder(requireContext())
                .setTitle("Privacy")
                .setMessage("We value your privacy. All your data is stored locally in the device and not sent to cloud.")
                .setPositiveButton("GOT IT") { dialog, _ ->
                    with(credentialsViewModel) {
                        addAccount(userName, password, true)
                        changeFirstOpenBoolean()
                        saveDisplayName(displayName)
                    }
                    findNavController(button_cred_next).popBackStack()
                    dialog.dismiss()
                }
                .show()
    }

}