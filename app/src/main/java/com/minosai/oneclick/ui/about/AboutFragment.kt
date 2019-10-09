package com.minosai.oneclick.ui.about

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.minosai.oneclick.BuildConfig
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants
import kotlinx.android.synthetic.main.fragment_about.view.*


class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(view) {

            toolbar_about_fragment.setNavigationOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

            text_about_version.text = "Version: ${BuildConfig.VERSION_NAME}"

            about_social_telegram.setOnClickListener {
                openUrl("https://t.me/joinchat/GRJ3QhK-ZkFPCAiOOjaCkQ")
            }

            about_other_telegram.setOnClickListener {
                openUrl("https://t.me/joinchat/AAAAAE66lc5LE8KxQZSFLg")
            }

            about_dev_github.setOnClickListener {
                openUrl("https://github.com/MINOSai/One-Click")
            }

            about_dev_store.setOnClickListener {
                try {
                    startActivity(Intent(
                            ACTION_VIEW,
                            Uri.parse("market://details?id=${Constants.PACKAGE_NAME}")
                    ))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(
                            ACTION_VIEW,
                            Uri.parse(Constants.PLAY_STORE_URL)
                    ))
                }
            }

            about_dev_share.setOnClickListener {
                val shareBody = "Hey check out this app which I use to login to VIT WiFi " +
                        Constants.PLAY_STORE_URL

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "OneClick - WiFi login app")
                    putExtra(Intent.EXTRA_TEXT, shareBody)
                }

                startActivity(Intent.createChooser(intent, "Share using"))
            }

            about_license.setOnClickListener {
                startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
            }

            about_member_yaswant.setOnClickListener {
                openUrl("https://www.linkedin.com/in/yaswant-narayan/")
            }

            about_member_somesh.setOnClickListener {
                openUrl("https://www.linkedin.com/in/someshks/")
            }
        }
    }

    private fun openUrl(url: String) =
        startActivity(Intent(ACTION_VIEW, Uri.parse(url)))

}