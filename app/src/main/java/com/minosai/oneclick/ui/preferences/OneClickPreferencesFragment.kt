package com.minosai.oneclick.ui.preferences

import android.app.Activity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.minosai.oneclick.R

class OneClickPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_oneclick, rootKey)

        val displayNamePreference = findPreference<EditTextPreference>("display_name")
        displayNamePreference.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

        val aboutPreference = findPreference<Preference>("about")
        aboutPreference.setOnPreferenceClickListener {
            findNavController(activity as Activity, R.id.toolbar_fref_fragment).navigate(R.id.action_settingsFragment_to_aboutFragment)
            true
        }

    }

}