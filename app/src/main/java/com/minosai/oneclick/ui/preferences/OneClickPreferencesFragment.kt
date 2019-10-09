package com.minosai.oneclick.ui.preferences

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.minosai.oneclick.MainActivity
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants

class OneClickPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_oneclick, rootKey)

        val displayNamePreference = findPreference<EditTextPreference>("display_name")
        displayNamePreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

        val darkThemePreference = findPreference<Preference>("dark_theme")
        darkThemePreference?.setOnPreferenceChangeListener { preference, newValue ->
            requireActivity().finish()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            true
        }

//        val ratePreference = findPreference<Preference>("rate")
//        ratePreference?.setOnPreferenceClickListener {
//            try {
//                startActivity(Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("market://details?id=${Constants.PACKAGE_NAME}")
//                ))
//            } catch (anfe: android.content.ActivityNotFoundException) {
//                startActivity(Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(Constants.PLAY_STORE_URL)
//                ))
//            }
//            true
//        }

        val sharePreference = findPreference<Preference>("share")
        sharePreference?.setOnPreferenceClickListener {
            val shareBody = "Hey check out this app which I use to login to VIT WiFi " +
                    Constants.PLAY_STORE_URL

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "OneClick - WiFi login app")
                putExtra(Intent.EXTRA_TEXT, shareBody)
            }

            startActivity(Intent.createChooser(intent, "Share using"))
            true
        }

        val aboutPreference = findPreference<Preference>("about")
        aboutPreference?.setOnPreferenceClickListener {
            findNavController(
                    activity as Activity,
                    R.id.toolbar_fref_fragment
            ).navigate(R.id.action_settingsFragment_to_aboutFragment)
            true
        }

//        val licensePreference = findPreference<Preference>("license")
//        licensePreference?.setOnPreferenceClickListener {
//            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
//            true
//        }
    }

}