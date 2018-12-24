package com.minosai.oneclick.ui.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.minosai.oneclick.R
import com.minosai.oneclick.adapter.AccountAdapter
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.ui.main.bottomsheets.InputBottomSheetFragment
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.listener.InputSheetListener
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.listener.WifiConnectivityListener
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import com.minosai.oneclick.util.service.WebService
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker
import com.treebo.internetavailabilitychecker.InternetConnectivityListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class MainFragment : Fragment(),
        Injectable,
        InternetConnectivityListener,
        WifiConnectivityListener,
        LoginLogoutListener,
        InputSheetListener {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    private lateinit var wifiReceiver: WifiReceiver
    private lateinit var loginLogoutReceiver: LoginLogoutReceiver
    private lateinit var mInternetAvailabilityChecker: InternetAvailabilityChecker
    private var activeAccount: AccountInfo? = null
    private var isLoading = false

    private val inputBottomSheetFragment = InputBottomSheetFragment()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AccountAdapter

    private lateinit var state: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wifiReceiver = WifiReceiver(this)
        registerWifiReceiver()
        try {
            mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
            mInternetAvailabilityChecker.addInternetConnectivityListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        text_home_displayname.text = "Hello, ${mainViewModel.displayName}"

        mainViewModel.view = coordinator_main

        adapter = AccountAdapter(context!!, mainViewModel) {
            Toast.makeText(context, it.username, Toast.LENGTH_SHORT).show()
        }

        initRecyclerView()
        addObservers()
        setClicks()
    }

    private fun initRecyclerView() {

        launch(UI) {
            val linearLayoutManager = LinearLayoutManager(activity)
            //TODO: Check below commented line
//            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            rv_accounts.layoutManager = linearLayoutManager
            rv_accounts.hasFixedSize()
            rv_accounts.adapter = adapter
        }
    }

    private fun addObservers() {

        mainViewModel.getAllAccounts().observe(this, Observer {
            activeAccount = mainViewModel.getActiveAccount()
            updateUi()
            adapter.updateList(it)
        })
    }

    private fun setClicks() {

        button_login.setOnClickListener {
            //            val userName = input_userid.text.toString()
//            val password = input_password.text.toString()
            webService.login(this, activeAccount?.username, activeAccount?.password)
//            saveUser(userName,  password)
            startLoading()
        }

        button_logout.setOnClickListener {
            webService.logout(this)
            startLoading()
        }

        button_incognito.setOnClickListener {
            inputBottomSheetFragment.init(this, Constants.SheetAction.INCOGNITO)
            inputBottomSheetFragment.show(fragmentManager!!, inputBottomSheetFragment.tag)
        }

        button_newuser.setOnClickListener {
            inputBottomSheetFragment.init(this, Constants.SheetAction.NEW_ACCOUNT)
            inputBottomSheetFragment.show(fragmentManager!!, inputBottomSheetFragment.tag)
        }

        button_refresh.setOnClickListener {
//            snackbar(mainViewModel.view, "Refresh account details")
            Snackbar.make(mainViewModel.view, "Refresh account details", Snackbar.LENGTH_SHORT).show()
        }

        button_sleep_timer.setOnClickListener {
//            Snackbar.make(coordinator_main, "Snack Bar", Snackbar.LENGTH_SHORT).show()
//            snackbar(mainViewModel.view, "Sleep timer")
            Snackbar.make(mainViewModel.view, "Sleep timer", Snackbar.LENGTH_SHORT).show()
        }

        button_main.setOnClickListener {
            when(mainViewModel.state) {
                Constants.ButtonAction.CONNECT -> connectWifi()
                Constants.ButtonAction.LOGIN -> {
                    webService.login(this, activeAccount?.username, activeAccount?.password)
                    button_main?.startAnimation()
                }
                Constants.ButtonAction.LOGOUT -> {
                    webService.logout(this)
                    button_main?.startAnimation()
                }
            }
        }

        button_settings.setOnClickListener {
            findNavController(it).navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

    private fun connectWifi() {
        openWifiSettings()
    }

    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        if (intent.resolveActivity(context?.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun stopButtonAnimation(text: String) {
        button_main?.revertAnimation {
            button_main.background = resources.getDrawable(R.drawable.shape_capsule)
//            button_main.text = text
        }
    }

    private fun showSuccess() {
        context?.let {
            val icon = BitmapFactory.decodeResource(it.resources, R.drawable.ic_done_white_48dp)
            button_main?.doneLoadingAnimation(android.R.color.white, icon)
            Handler().postDelayed({
                stopButtonAnimation("")
            }, 1000)
        }
    }

    private fun showFailure() {
        context?.let {
            //TODO: Remove this try catch
            try {
                val icon = BitmapFactory.decodeResource(it.resources, R.drawable.ic_close_black_24dp)
                button_main?.doneLoadingAnimation(android.R.color.white, icon)
                Handler().postDelayed({
                    stopButtonAnimation("")
                }, 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        try {
            mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        unregisterWifiReceiver()
        button_main?.dispose()
        super.onDestroyView()
    }

    private fun registerWifiReceiver() {
        try {
            val intentFilter = IntentFilter()
            with(intentFilter) {
                addAction("android.net.wifi.WIFI_SATE_CHANGED")
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
                addAction("android.net.wifi.STATE_CHANGE")
            }
            context?.registerReceiver(wifiReceiver, intentFilter)
            Log.d(TAG, "WifiReceiver registered")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "WifiReceiver already registered")
        }
    }

    private fun unregisterWifiReceiver() {
        try {
            context?.unregisterReceiver(wifiReceiver)
        } catch (e: Exception) {
            // already unregistered
        }
    }

    private fun updateUi() {

        activeAccount?.let {
            text_home_username.text = it.username
            text_home_usage.text = it.usage
            text_home_usage.text = it.usage
        }
    }

    private fun saveUser(userName: String, password: String) {
        preferences.edit()
                .putString(Constants.PREF_USERNAME, userName)
                .putString(Constants.PREF_PASSWORD, password)
                .apply()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        mainViewModel.isOnline = isConnected
        updateState()
    }

    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
        mainViewModel.isWifiConnected = isConnectedToWifi
        mainViewModel.ssid = ssid
        updateState()
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean) {
//        stopButtonAnimation("")
        stopLoading()
        mainViewModel.isOnline = isLogged
        when (requestType) {
            WebService.Companion.RequestType.LOGIN -> {
                mainViewModel.isOnline = isLogged
                if (isLogged) {
                    showSuccess()
//                    snackbar(mainViewModel.view, "Successfully logged in")
                    Snackbar.make(mainViewModel.view, "Successfully logged in", Snackbar.LENGTH_SHORT).show()
                    if (mainViewModel.isAutoUpdateUsage()) {
                        startLoading()
                        webService.getUsage { usage ->
                            mainViewModel.updateUsage(usage)
                        }
                    }
                } else {
                    showFailure()
//                    snackbar(mainViewModel.view, "Login failed", "retry") {
//                        webService.login(this, activeAccount?.username, activeAccount?.password)
//                    }
                    Snackbar.make(mainViewModel.view, "Login failed", Snackbar.LENGTH_SHORT).setAction("retry") {
                        webService.login(this, activeAccount?.username, activeAccount?.password)
                    }.show()
                }
            }
            WebService.Companion.RequestType.LOGOUT -> {
                if (isLogged) {
                    showSuccess()
//                    snackbar(mainViewModel.view, "Successfully logged out")
                    Snackbar.make(mainViewModel.view, "Successfully logged out", Snackbar.LENGTH_SHORT).show()
                } else {
                    showFailure()
//                    snackbar(mainViewModel.view, "Logout failed", "retry") {
//                        webService.logout(this)
//                    }
                    Snackbar.make(mainViewModel.view, "Logout failed", Snackbar.LENGTH_SHORT).setAction("retry") {
                        webService.logout(this)
                    }.show()
                }
                if (mainViewModel.isOnline && isLogged) {
                    mainViewModel.isOnline = false
                }
            }
        }
        updateState()
    }

    override fun onSheetResponse(userName: String, password: String, isActiveAccount: Boolean, action: Constants.SheetAction) {
        when(action) {
            Constants.SheetAction.NEW_ACCOUNT -> mainViewModel.addUser(userName, password, isActiveAccount)
            else -> {

            }
        }
    }

    private fun updateState() {
        if (mainViewModel.isWifiConnected) {
            if (mainViewModel.isOnline) {
                button_main?.text = "Logout"
//                stopButtonAnimation("Logout")
                mainViewModel.state = Constants.ButtonAction.LOGOUT
            } else {
                button_main?.text = "Login"
//                stopButtonAnimation("Login")
                mainViewModel.state = Constants.ButtonAction.LOGIN
            }
        } else {
//            button_main?.text = "connnect to wifi"
            stopButtonAnimation("Connect to wifi")
            mainViewModel.state = Constants.ButtonAction.CONNECT
        }
    }

    private fun toggleLoading() {
        if (isLoading) {
            stopLoading()
        } else {
            startLoading()
        }
    }

    private fun startLoading() {
        if (!isLoading) {
            isLoading = true
            //TODO: Start loading animation
        }
    }

    private fun stopLoading() {
        if (isLoading) {
            isLoading = false
            //TODO: Stop loading animation
        }
    }

}