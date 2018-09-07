package com.minosai.oneclick.util

import android.content.Context
import android.content.res.ColorStateList
import android.net.wifi.WifiManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.minosai.oneclick.R

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.setBackgroundTint(colorInt: Int) {
    this.backgroundTintList = ColorStateList.valueOf(resources.getColor(colorInt))
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.toggleVisibility(imageView: ImageView) {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
        imageView.setImageResource(R.drawable.ic_chevron_down)
    } else {
        this.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.ic_chevron_up)
    }
}

fun Context.getSSID(): String? {
    val wifiManager = this.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val info = wifiManager?.connectionInfo
    return info?.ssid
}