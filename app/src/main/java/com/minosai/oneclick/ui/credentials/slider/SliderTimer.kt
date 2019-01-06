package com.minosai.oneclick.ui.credentials.slider

import android.app.Activity
import androidx.viewpager.widget.ViewPager
import java.util.*

class SliderTimer(val activity: Activity?, val viewPager: ViewPager, val size: Int) : TimerTask() {

    override fun run() {
        activity?.runOnUiThread {
            if (viewPager.currentItem < size - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                viewPager.currentItem = 0
            }
        }
    }

}