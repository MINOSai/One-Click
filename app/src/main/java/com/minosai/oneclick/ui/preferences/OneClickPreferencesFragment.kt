package com.minosai.oneclick.ui.preferences

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.minosai.oneclick.R

class OneClickPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_oneclick, rootKey)

        val displayNamePreference = findPreference("display_name") as EditTextPreference
        displayNamePreference.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

    }

}