package com.minosai.oneclick.ui.dialog.bottomsheets

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.minosai.oneclick.R
import com.minosai.oneclick.ui.adapter.InfoBulletAdapter
import com.minosai.oneclick.ui.adapter.InfoNumberAdapter
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.hide
import com.minosai.oneclick.util.show
import kotlinx.android.synthetic.main.fragment_info.*

class InfoBottomSheetFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_info_fragment.hide()

        val noteAdapter = InfoBulletAdapter(listOf(
                "This app will not work properly if mobile data enabled. Kindly disable mobile data before logging in.",
                "Your PRIMARY account details will be used to login in QS tile and home screen widget.",
                "Due to limitation in Android OS ecosystem the connected WiFi network SSID cannot be checked so the app will show 'LOGIN' even if you are not connected to VIT WiFi network."
        ))

        val qsTileAdapter = InfoNumberAdapter(listOf(
                "Open Quick Settings (Expand notification panel).",
                "Tap the PENCIL icon (This will vary for different manufacturers).",
                "Tap and drag 'One Click' setting to where you want it."
        ))

        val widgetAdapter = InfoNumberAdapter(listOf(
                "Tap and hold an empty section of your Home screen.",
                "At the bottom (or dialog box in new versions), tap Widgets.",
                "Scroll until you see the 'One Click' widget.",
                "Tap and hold the widget and drag it to the home screen."
        ))

        toolbar_info_fragment.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        with(rv_info_note) {
            layoutManager = LinearLayoutManager(activity)
            hasFixedSize()
            adapter = noteAdapter
        }
        ViewCompat.setNestedScrollingEnabled(rv_info_note, false)

        with(rv_info_qstile) {
            layoutManager = LinearLayoutManager(activity)
            hasFixedSize()
            adapter = qsTileAdapter
        }
        ViewCompat.setNestedScrollingEnabled(rv_info_qstile, false)

        with(rv_info_widget) {
            layoutManager = LinearLayoutManager(activity)
            hasFixedSize()
            adapter = widgetAdapter
        }
        ViewCompat.setNestedScrollingEnabled(rv_info_widget, false)

        if (Build.MANUFACTURER == "OnePlus") {
            info_battery_optimization.show()
            info_button_app_info.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${Constants.PACKAGE_NAME}")
                }
                startActivity(intent)
            }
        }

        info_image_close.setOnClickListener {
            dismiss()
        }

    }

}