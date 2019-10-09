package com.minosai.oneclick.ui.main

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
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
    private var isConnectedToWifi = false
    private lateinit var inputSheetListener: InputSheetListener
    private var activeAccount: AccountInfo? = null
    private var isLoading = false
    private lateinit var mainViewModel: MainViewModel
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wifiReceiver = WifiReceiver(this)
        registerWifiReceiver()

        inputSheetListener = this

        mainViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(MainViewModel::class.java)

        mainViewModel.view = view.coordinator_main

        mainViewModel.updateUserPrefs()

        var displayName = mainViewModel.userPrefs.displayName
        if (displayName.isEmpty() || displayName.isBlank()) {
            displayName = "VITian"
        }
        view.text_home_displayname?.text = "Hello, ${displayName}"

        initRecyclerView(view)
        addObservers()
        setClicks(view)
    }

    private fun initRecyclerView(view: View) {

        accountAdapter = AccountAdapter { action, account ->
            when (action) {
                Constants.AccountAction.SET_PRIMARY -> {
                    showDialogToPrimary(account)
                }
                Constants.AccountAction.LOGIN -> {
                    login(account.username, account.password)
                }
                Constants.AccountAction.COPY_PASSWORD -> {
                    copyToClipboard(account.password)
                }
                Constants.AccountAction.VIEW_PASSWORD -> {
                    showDialog(account.password)
                }
                Constants.AccountAction.EDIT_ACCOUNT -> {
                    showBottomSheet(
                            fragmentManager!!,
                            Constants.SheetAction.EDIT_ACCOUNT,
                            account
                    )
                }
                Constants.AccountAction.DELETE_ACCOUNT -> {
                    showDialogToDelete(account)
                }
            }
        }

        with(view.rv_accounts) {
            layoutManager = LinearLayoutManager(activity)
            hasFixedSize()
            adapter = accountAdapter
        }
        ViewCompat.setNestedScrollingEnabled(view.rv_accounts, false)
    }

    private fun showDialogToPrimary(account: AccountInfo) {
        AlertDialog.Builder(requireContext())
                .setTitle("Make primary")
                .setMessage("Make the account with username '${account.username}' as primary account?")
                .setPositiveButton("YES") { dialog, _ ->
                    mainViewModel.setPrimaryAccount(account.id)
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showDialogToDelete(account: AccountInfo) {
        AlertDialog.Builder(requireContext())
                .setTitle("Delete account")
                .setMessage("Are you sure you want to delete account with username '${account.username}'?")
                .setPositiveButton("YES") { dialog, _ ->
                    mainViewModel.removeAccount(account)
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showDialog(password: String) {
        AlertDialog.Builder(requireContext())
                .setTitle("Password")
                .setMessage(password)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun copyToClipboard(password: String) {
        val clipboard = requireContext()
                .getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("account password", password)
        clipboard.setPrimaryClip(clip)
        showSnackBar(R.drawable.ic_check_circle, "Password copied to clipboard")
    }

    private fun addObservers() {

        mainViewModel.getAllAccounts().observe(this, Observer {
            activeAccount = mainViewModel.getActiveAccount()
            accountAdapter.updateList(it)

            if (mainViewModel.userPrefs.loginAppStart &&
                    !mainViewModel.loginAttempted &&
                    activeAccount != null) {
                login(activeAccount!!.username, activeAccount!!.password)
                mainViewModel.loginAttempted = true
            }
        })

        mainViewModel.isLoading.observe(this, Observer { isLoading ->
            this.isLoading = isLoading
            if (isLoading) {
                startLoading()
            } else {
                stopLoading()
            }
        })
    }

    private fun setClicks(view: View) {

        view.button_login?.setOnClickListener {
            activeAccount?.let {
                login(it.username, it.password)
            }
        }

        view.button_logout?.setOnClickListener {
            logout()
        }

        view.fab_action_incognito?.setOnClickListener {
            showBottomSheet(fragmentManager!!, Constants.SheetAction.INCOGNITO, null)
        }

        view.fab_action_newacc?.setOnClickListener {
            showBottomSheet(fragmentManager!!, Constants.SheetAction.NEW_ACCOUNT, null)
        }

        view.fab_action_settings?.setOnClickListener {
            findNavController(it).navigate(com.minosai.oneclick.R.id.action_mainFragment_to_settingsFragment)
        }

        view.fab_action_sleep_timer?.setOnClickListener {
//            showSnackBar("Sleep timer")
        }

        view.button_wifi?.setOnClickListener {
            //            openWifiSettings()
            expandNotification()
        }

        view.main_icon_info.setOnClickListener {
            findNavController(it).navigate(com.minosai.oneclick.R.id.action_mainFragment_to_infoFragment)
        }
    }

    private fun login(userName: String, password: String) {
        if (isConnectedToWifi) {
            if (!isConnectedToMobileData()) {
                if (!isLoading) {
                    webService.login(this, userName, password)
                    mainViewModel.startLoading()
                }
            } else {
                showSnackBar(R.drawable.ic_signal_off, "Please turn off mobile data")
            }
        } else {
            showSnackBar(R.drawable.ic_wifi_off, "Not connected to WiFi")
        }
    }

    private fun logout() {
        if (isConnectedToWifi) {
            if (!isConnectedToMobileData()) {
                if (!isLoading) {
                    webService.logout(this)
                    mainViewModel.startLoading()
                }
            } else {
                showSnackBar(R.drawable.ic_signal_off, "Please turn off mobile data")
            }
        } else {
            showSnackBar(R.drawable.ic_wifi_off, "Not connected to WiFi")
        }
    }

    private fun isConnectedToMobileData(): Boolean {
        val cm = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
        return false
    }

    private fun expandNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startActivity(Intent(Settings.Panel.ACTION_WIFI))
        } else {
            try {
                val service = requireContext().getSystemService("statusbar")
                val statusbarManager = Class.forName("android.app.StatusBarManager")
                val expand = statusbarManager.getMethod("expandNotificationsPanel")
                expand.invoke(service)
            } catch (e: Exception) {
                openWifiSettings()
            }
        }
    }

    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        unregisterWifiReceiver()
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

    private fun saveUser(userName: String, password: String) {
        preferences.edit()
                .putString(Constants.PREF_USERNAME, userName)
                .putString(Constants.PREF_PASSWORD, password)
                .apply()
    }

    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
        this.isConnectedToWifi = isConnectedToWifi
        if (isConnectedToWifi) {
            layout_with_wifi.show()
            layout_without_wifi.hide()
        } else {
            layout_with_wifi.hide()
            layout_without_wifi.show()
        }
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType,
                                  isLogged: Boolean,
                                  responseString: String) {
        mainViewModel.stopLoading()
        showSnackBar(
                if (isLogged) R.drawable.ic_check_circle else R.drawable.ic_alert_circle,
                responseString
        )
    }

    override fun onSheetResponse(userName: String,
                                 password: String,
                                 isActiveAccount: Boolean,
                                 action: Constants.SheetAction,
                                 accountInfo: AccountInfo?) {
        when (action) {
            Constants.SheetAction.NEW_ACCOUNT -> {
                mainViewModel.addUser(userName, password, isActiveAccount)
            }
            Constants.SheetAction.INCOGNITO -> {
                login(userName, password)
            }
            Constants.SheetAction.EDIT_ACCOUNT -> {
                accountInfo?.apply {
                    this.username = userName
                    this.password = password
                    mainViewModel.updateAccInfo(this)
                }
//                addObservers()
            }
        }
    }

    private fun startLoading() {
        main_layout_alert?.hide()
        main_wifi_loading_anim.apply {
            show()
            playAnimation()
        }
    }

    private fun stopLoading() {
        main_wifi_loading_anim.apply {
            cancelAnimation()
            visibility = View.INVISIBLE
            hide()
        }
    }

    private fun showBottomSheet(fragMgr: FragmentManager,
                                action: Constants.SheetAction,
                                accountInfo: AccountInfo?) {
        InputBottomSheetFragment().apply {
            init(inputSheetListener, action, accountInfo)
            show(fragMgr, this.tag)
        }
    }

    private fun showSnackBar(drawable: Int, message: String) {
//        getSnackBar(message).show()
        main_layout_alert?.show()
        main_text_alert?.apply {
            text = message
            setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
        }

        Handler().postDelayed({
            main_layout_alert?.hide()
        }, 1500)
    }

    private fun getSnackBar(message: String) =
            Snackbar.make(mainViewModel.view, message, Snackbar.LENGTH_SHORT)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        main_icon_inappupdate.onActivityResult(requestCode, resultCode)
    }
}
