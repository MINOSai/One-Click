package com.minosai.oneclick.ui.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.util.Constants
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment(), Injectable {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.toolbar_fref_fragment.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp)

        view.toolbar_fref_fragment.setNavigationOnClickListener {
            findNavController(it).popBackStack()
        }

        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.settings_container, OneClickPreferencesFragment())
                ?.commit()

        return view
    }
}

