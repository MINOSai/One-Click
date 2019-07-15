package com.minosai.oneclick.ui.main

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.ui.adapter.AccountAdapter
import com.minosai.oneclick.ui.dialog.bottomsheets.InputBottomSheetFragment
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.hide
import com.minosai.oneclick.util.listener.InputSheetListener
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.listener.WifiConnectivityListener
import com.minosai.oneclick.util.receiver.WifiReceiver
import com.minosai.oneclick.util.show
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import javax.inject.Inject


class MainFragment : Fragment(),
        Injectable,
//        InternetConnectivityListener,
        WifiConnectivityListener,
        LoginLogoutListener,
        InputSheetListener {

    val TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    private lateinit var wifiReceiver: WifiReceiver
    //    private lateinit var loginLogoutReceiver: LoginLogoutReceiver
//    private lateinit var mInternetAvailabilityChecker: InternetAvailabilityChecker
    private lateinit var inputSheetListener: InputSheetListener
    private var activeAccount: AccountInfo? = null
    private var isLoading = false

//    private val inputBottomSheetFragment = InputBottomSheetFragment()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var accountAdapter: AccountAdapter

    private lateinit var state: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wifiReceiver = WifiReceiver(this)
        registerWifiReceiver()
//        try {
//            mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
//            mInternetAvailabilityChecker.addInternetConnectivityListener(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        inputSheetListener = this

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.view = view.coordinator_main

        mainViewModel.updateUserPrefs()

        var displayName = mainViewModel.userPrefs.displayName
        if (displayName.isEmpty() || displayName.isBlank()) {
            displayName = "User"
        }
        view.text_home_displayname?.text = "Hello, ${displayName}"

        accountAdapter = AccountAdapter(context!!, mainViewModel) {

            showBottomSheet(fragmentManager!!, Constants.SheetAction.EDIT_ACCOUNT, it)

//            inputBottomSheetFragment.init(this, Constants.SheetAction.EDIT_ACCOUNT, it)
//            inputBottomSheetFragment.show(fragmentManager!!, inputBottomSheetFragment.tag)
        }

        initRecyclerView(view)
        addObservers()
        setClicks(view)
    }

    private fun initRecyclerView(view: View) {

        val linearLayoutManager = LinearLayoutManager(activity)
        //TODO: Check below commented line
//            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        with(view.rv_accounts) {
            layoutManager = linearLayoutManager
            hasFixedSize()
            adapter = accountAdapter
        }
    }

    private fun addObservers() {

        mainViewModel.getAllAccounts().observe(this, Observer {
            activeAccount = mainViewModel.getActiveAccount()
            updateUi()
            accountAdapter.updateList(it)
        })

        mainViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                startLoading()
            } else {
                stopLoading()
            }
        })
    }

    private fun setClicks(view: View) {

        view.button_login?.setOnClickListener {
            //            val userName = input_userid.text.toString()
//            val password = input_password.text.toString()
            webService.login(this, activeAccount?.username, activeAccount?.password)
//            saveUser(userName,  password)
            it.startAnimation(getAnimation())
            startLoading()
        }

        view.button_logout?.setOnClickListener {
            webService.logout(this)
            it.startAnimation(getAnimation())
            startLoading()
        }

        view.fab_action_incognito?.setOnClickListener {
            //            inputBottomSheetFragment.init(this, , mainViewModel.getActiveAccount())
//            inputBottomSheetFragment.show(fragmentManager!!, inputBottomSheetFragment.tag)
            showBottomSheet(fragmentManager!!, Constants.SheetAction.INCOGNITO, null)
        }

        view.fab_action_newacc?.setOnClickListener {
            //            inputBottomSheetFragment.init(this, Constants.SheetAction.NEW_ACCOUNT, mainViewModel.getActiveAccount())
//            inputBottomSheetFragment.show(fragmentManager!!, inputBottomSheetFragment.tag)
            showBottomSheet(fragmentManager!!, Constants.SheetAction.NEW_ACCOUNT, null)
        }

        view.fab_action_settings?.setOnClickListener {
            //            snackbar(mainViewModel.view, "Refresh account details")
//            Snackbar.make(mainViewModel.view, "Refresh account details", Snackbar.LENGTH_SHORT).show()
            findNavController(it).navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        view.fab_action_sleep_timer?.setOnClickListener {
            //            Snackbar.make(coordinator_main, "Snack Bar", Snackbar.LENGTH_SHORT).show()
//            snackbar(mainViewModel.view, "Sleep timer")
            Snackbar.make(mainViewModel.view, "Sleep timer", Snackbar.LENGTH_SHORT).show()
        }

        view.button_wifi?.setOnClickListener {
            openWifiSettings()
        }

        view.button_settings?.setOnClickListener {
            findNavController(it).navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

/*    private fun stopButtonAnimation(text: String) {
        button_main?.revertAnimation {
            button_main.background = resources.getDrawable(R.drawable.shape_capsule)
//            button_main.text = text
        }
    }*/

    private fun showSuccess() {
//        context?.let {
//            val icon = BitmapFactory.decodeResource(it.resources, R.drawable.ic_done_white_48dp)
//            button_main?.doneLoadingAnimation(android.R.color.white, icon)
//            Handler().postDelayed({
//                stopButtonAnimation("")
//            }, 1000)
//        }
    }

    private fun showFailure() {
//        context?.let {
//            //TODO: Remove this try catch
//            try {
//                val icon = BitmapFactory.decodeResource(it.resources, R.drawable.ic_close_black_24dp)
//                button_main?.doneLoadingAnimation(android.R.color.white, icon)
//                Handler().postDelayed({
//                    stopButtonAnimation("")
//                }, 1000)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }

    override fun onDestroyView() {
//        try {
//            mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        unregisterWifiReceiver()
//        button_main?.dispose()
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

//        activeAccount?.let {
//            text_home_username.text = it.username
//            text_home_usage.text = it.usage
//            text_home_usage.text = it.usage
//        }
    }

    private fun saveUser(userName: String, password: String) {
        preferences.edit()
                .putString(Constants.PREF_USERNAME, userName)
                .putString(Constants.PREF_PASSWORD, password)
                .apply()
    }

/*    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        mainViewModel.isOnline = isConnected
        updateState()
    }*/

    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
//        mainViewModel.isWifiConnected = isConnectedToWifi
//        mainViewModel.ssid = ssid
//        updateState()
        if (isConnectedToWifi) {
            layout_with_wifi.show()
            layout_without_wifi.hide()
        } else {
            layout_with_wifi.hide()
            layout_without_wifi.show()
        }
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean, responseString: String) {
//        stopButtonAnimation("")
        stopLoading()
//        mainViewModel.isOnline = isLogged
        when (requestType) {
            WebService.Companion.RequestType.LOGIN -> {
                button_login.clearAnimation()
//                mainViewModel.isOnline = isLogged
                if (isLogged) {
                    showSuccess()
//                    snackbar(mainViewModel.view, "Successfully logged in")
                    Snackbar.make(mainViewModel.view, responseString, Snackbar.LENGTH_SHORT).show()
                    if (mainViewModel.userPrefs.autoRefresh) {
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
                    Snackbar.make(mainViewModel.view, responseString, Snackbar.LENGTH_SHORT).setAction("retry") {
                        webService.login(this, activeAccount?.username, activeAccount?.password)
                    }.show()
                }
            }
            WebService.Companion.RequestType.LOGOUT -> {
                button_logout.clearAnimation()
                if (isLogged) {
                    showSuccess()
//                    snackbar(mainViewModel.view, "Successfully logged out")
                    Snackbar.make(mainViewModel.view, responseString, Snackbar.LENGTH_SHORT).show()
                } else {
                    showFailure()
//                    snackbar(mainViewModel.view, "Logout failed", "retry") {
//                        webService.logout(this)
//                    }
                    Snackbar.make(mainViewModel.view, responseString, Snackbar.LENGTH_SHORT).setAction("retry") {
                        webService.logout(this)
                    }.show()
                }
//                if (mainViewModel.isOnline && isLogged) {
//                    mainViewModel.isOnline = false
//                }
            }
        }
//        updateState()
    }

    override fun onSheetResponse(userName: String, password: String, isActiveAccount: Boolean, action: Constants.SheetAction, accountInfo: AccountInfo?) {
        when (action) {
            Constants.SheetAction.NEW_ACCOUNT -> mainViewModel.addUser(userName, password, isActiveAccount)
            Constants.SheetAction.INCOGNITO -> webService.login(this, userName, password)
            Constants.SheetAction.EDIT_ACCOUNT -> {
                accountInfo?.let {
                    it.username = userName
                    it.password = password
                    mainViewModel.updateAccInfo(it)
                    addObservers()
                }
            }
        }
    }

    private fun updateState() {
//        if (mainViewModel.isWifiConnected) {
//            if (mainViewModel.isOnline) {
//                button_wifi?.text = "Logout"
////                stopButtonAnimation("Logout")
//                mainViewModel.state = Constants.ButtonAction.LOGOUT
//            } else {
//                button_wifi?.text = "Login"
////                stopButtonAnimation("Login")
//                mainViewModel.state = Constants.ButtonAction.LOGIN
//            }
//        } else {
////            button_main?.text = "connnect to wifi"
////            stopButtonAnimation("Connect to wifi")
//            mainViewModel.state = Constants.ButtonAction.CONNECT
//        }
    }

/*    private fun toggleLoading() {
        if (isLoading) {
            stopLoading()
        } else {
            startLoading()
        }
    }*/

    private fun startLoading() {
        main_view_loading.show()
    }

    private fun stopLoading() {
        main_view_loading.hide()
    }

    private fun getAnimation() = AlphaAnimation(1f, 0.5f).apply {
        duration = 500
        interpolator = LinearInterpolator()
        repeatCount = Animation.INFINITE
        repeatMode = Animation.REVERSE
    }


    private fun showBottomSheet(fragMgr: FragmentManager, action: Constants.SheetAction, accountInfo: AccountInfo?) {
        InputBottomSheetFragment().apply {
            init(inputSheetListener, action, accountInfo)
            show(fragMgr, this.tag)
        }
    }

}