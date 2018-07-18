package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import dagger.android.AndroidInjection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)

        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("H:mm:ss")
        val time = dateFormat.format(cal.time)
        val currentTime = dateFormat.parse(time)
        val logoutTime = dateFormat.parse("0:30:00")
        val loginTime = dateFormat.parse("4:30:00")
        currentTime.after(logoutTime)
    }
}