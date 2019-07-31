package com.minosai.oneclick.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.minosai.oneclick.R
import com.minosai.oneclick.ui.adapter.InfoBulletAdapter
import com.minosai.oneclick.ui.adapter.InfoNumberAdapter
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteAdapter = InfoBulletAdapter(listOf(
                "This app will not work properly if mobile data enabled. Kindly disable mobile data before logging in.",
                "Your PRIMARY account details will be used to login in QS tile and home screen widget.",
                "Due to limitation in Android OS ecosystem the connected WiFi network SSID cannot be checked so the app will show 'LOGIN' even if you are not connected to VIT WiFi network."
        ))

        val qsTileAdapter = InfoNumberAdapter(listOf(
                "Open Quick Settings (Expand notification panel).",
                "Tap the PENCIL icon (or if you’re using a Samsung device, the 3 vertical dot icon -> edit/reorder).",
                "Tap and drag 'One Click' setting to where you want it."
        ))

        val widgetAdapter = InfoNumberAdapter(listOf(
                "Tap and hold an empty section of your Home screen.",
                "At the bottom (or dialog box in new versions), tap Widgets.",
                "Scroll until you see the 'One Click' widget.",
                "Tap and hold the widget and drag it to the home screen."
        ))

        with(view) {

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
        }
    }
}