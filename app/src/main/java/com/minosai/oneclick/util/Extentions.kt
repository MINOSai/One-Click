package com.minosai.oneclick.util

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.net.wifi.WifiManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

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

fun View.toggleVisibility() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
//        imageView.setImageResource(R.drawable.ic_chevron_down)
    } else {
        this.visibility = View.VISIBLE
//        imageView.setImageResource(R.drawable.ic_chevron_up)
    }
}

fun Context.getSSID(): String? {
    val wifiManager = this.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val info = wifiManager?.connectionInfo
    return info?.ssid
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}