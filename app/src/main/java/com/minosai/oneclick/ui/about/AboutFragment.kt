package com.minosai.oneclick.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.minosai.oneclick.R
import kotlinx.android.synthetic.main.fragment_about.view.*


class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        view.toolbar_about_fragment.setNavigationOnClickListener {
            findNavController(it).popBackStack()
        }

        with(view) {

            about_social_telegram.setOnClickListener {

            }

            about_social_discord.setOnClickListener {

            }

            about_dev_github.setOnClickListener {

            }

            about_dev_store.setOnClickListener {
                val appPackageName = context.packageName
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }

            about_dev_share.setOnClickListener {

            }

            about_other_telegram.setOnClickListener {

            }

            about_other_license.setOnClickListener {

            }

            about_member_yaswant.setOnClickListener {

            }

            about_member_somesh.setOnClickListener {

            }
        }

        return view
    }

}