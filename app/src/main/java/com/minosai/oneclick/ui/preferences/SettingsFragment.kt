package com.minosai.oneclick.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.util.Constants

class SettingsFragment : Fragment(), Injectable {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.settings_container, OneClickPreferencesFragment())
                ?.commit()

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}

