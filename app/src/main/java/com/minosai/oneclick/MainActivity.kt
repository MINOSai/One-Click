package com.minosai.oneclick

import android.content.BroadcastReceiver
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.Navigation
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import android.net.wifi.WifiManager
import android.content.IntentFilter
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import com.minosai.oneclick.util.service.WifiService
import dagger.android.HasBroadcastReceiverInjector


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        try {
//            val intentFilter = IntentFilter()
//            with(intentFilter) {
//                addAction("android.net.wifi.WIFI_SATE_CHANGED")
//                addAction("android.net.conn.CONNECTIVITY_CHANGE")
//                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
//                addAction("android.net.wifi.STATE_CHANGE")
//            }
//            registerReceiver(WifiReceiver(), intentFilter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        startService(Intent(this, WifiService::class.java))

        val shortcut = ShortcutInfo.Builder(this, "shordcut_dynamic_login_logout")
                .setShortLabel("Login/Logout")
                .setLongLabel("Login or Logout")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_login))
                .setIntent(Intent(LoginLogoutBroadcastHelper.LOGIN_LOGOUT_ACTION, Uri.EMPTY, this, LoginLogoutReceiver::class.java))
                .build()

        val shortcutManager = getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = listOf(shortcut)
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(this, R.id.fragment_nav_host).navigateUp()
}
