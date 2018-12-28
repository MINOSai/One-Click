package com.minosai.oneclick

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var preferences: SharedPreferences

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        findNavController(R.id.fragment_nav_host).addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.mainFragment -> {
                    val isFirstTime = preferences[Constants.PREF_IS_FIRST_TIME, true] ?: true
                    if (isFirstTime) {
                        controller.navigate(R.id.action_mainFragment_to_credentialsFragment2)
                    }
                }
            }
        }
//        findNavController(R.id.fragment_nav_host).addOnNavigatedListener { controller, destination ->
//            when(destination.id) {
//                R.id.mainFragment -> {
//                    val isFirstTime = preferences[Constants.PREF_IS_FIRST_TIME, true] ?: true
//                    if (isFirstTime) {
//                        controller.navigate(R.id.action_mainFragment_to_credentialsFragment2)
//                    }
//                }
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean = Navigation.findNavController(this, R.id.fragment_nav_host).navigateUp()

    override fun onBackPressed() {
        val currentDestination = findNavController(R.id.fragment_nav_host).currentDestination
        when (currentDestination?.id) {
            R.id.credentialsFragment -> {
                finish()
            }
        }
        super.onBackPressed()
    }
}
