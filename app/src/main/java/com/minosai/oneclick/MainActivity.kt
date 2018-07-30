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
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.minosai.oneclick.ui.fragment.credentials.CredentialsFragment
import com.minosai.oneclick.ui.fragment.main.MainFragment
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import com.minosai.oneclick.util.service.WifiService
import dagger.android.HasBroadcastReceiverInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var preferences: SharedPreferences

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        startService(Intent(this, WifiService::class.java))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            addAppShortcut()
        }

        checkFirstTime()

    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun addAppShortcut() {
        val shortcut = ShortcutInfo.Builder(this, "shordcut_dynamic_login_logout")
                .setShortLabel("Login/Logout")
                .setLongLabel("Login or Logout")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_login))
                .setIntent(Intent(LoginLogoutBroadcastHelper.LOGIN_LOGOUT_ACTION, Uri.EMPTY, this, LoginLogoutReceiver::class.java))
                .build()

        val shortcutManager = getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = listOf(shortcut)
    }

    private fun checkFirstTime() {
        findNavController(R.id.fragment_nav_host).addOnNavigatedListener { controller, destination ->
            when(destination.id) {
                R.id.mainFragment -> {
                    if (preferences.getBoolean(Constants.PREF_IS_FIRST_TIME, true)) {
                        controller.navigate(R.id.action_mainFragment_to_credentialsFragment2)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(this, R.id.fragment_nav_host).navigateUp()

    override fun onBackPressed() {
        val currentDestination = findNavController(R.id.fragment_nav_host).currentDestination
        when (currentDestination.id) {
            R.id.credentialsFragment -> {
                finish()
            }
        }
        super.onBackPressed()
    }
}
