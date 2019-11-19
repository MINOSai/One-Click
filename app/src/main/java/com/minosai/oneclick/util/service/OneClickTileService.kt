package com.minosai.oneclick.util.service

import android.annotation.TargetApi
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.listener.LoginLogoutListener
import dagger.android.AndroidInjection
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
class OneClickTileService : TileService(),
        LoginLogoutListener {

    val TAG = "oneclicktileservice_log"

    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

    private var activeAccount: AccountInfo? = null

    private val observer = Observer<AccountInfo> {
        Log.d(TAG, "accountObserver")
        activeAccount = it
        if (repoInterface.userPrefs.loginQsTile) {
            login()
        }
    }

//    override fun onCreate() {
//        super.onCreate()
//
//        AndroidInjection.inject(this)
//        Log.d(TAG, "onCreate")
//    }

//    override fun onTileAdded() {
//        super.onTileAdded()
//        AndroidInjection.inject(this)
//        repoInterface.activeAccount.observeForever(observer)
//        changeStateNormal()
//        Log.d(TAG, "onTileAdded")
//    }
//
//    override fun onTileRemoved() {
//        super.onTileRemoved()
//        repoInterface.activeAccount.removeObserver(observer)
//        Log.d(TAG, "onTileRemoved")
//    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d(TAG, "onStartListening")
        AndroidInjection.inject(this)
        repoInterface.activeAccount.observeForever(observer)
        changeStateNormal()
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d(TAG, "onStopListening")
        repoInterface.activeAccount.removeObserver(observer)
    }

    override fun onClick() {
        Log.d(TAG, "onClick")
        if (qsTile.state == Tile.STATE_INACTIVE && activeAccount != null) {
            login()
        }
    }

    private fun login() {
        try {
            webService.login(
                    this,
                    activeAccount!!.username,
                    activeAccount!!.password
            )
            changeStateLoading()
        } catch (e: Exception) {
            Crashlytics.logException(e)
        }
    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {

        Log.d(TAG, "loginResponse")

        if (requestType == RequestType.LOGIN) {
            if (isLogged) {
                changeStateSuccess()
            } else {
                changeStateFailure()
            }
        }

        Handler().postDelayed({
            changeStateNormal()
        }, 1500)
    }

    private fun changeStateNormal() = with(qsTile) {
        state = Tile.STATE_INACTIVE
        label = "Login"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_login
        )
        updateTile()
        Log.d(TAG, "stateNormal")
    }

    private fun changeStateLoading() = with(qsTile) {
        state = Tile.STATE_UNAVAILABLE
        label = "Loading"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_dots_horizontal
        )
        updateTile()
        Log.d(TAG, "stateLoading")
    }

    private fun changeStateSuccess() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Success"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_done_white_48dp
        )
        updateTile()
        Log.d(TAG, "stateSuccess")
    }

    private fun changeStateFailure() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Failed"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_error_outline_black_24dp
        )
        updateTile()
        Log.d(TAG, "stateFailure")
    }
}
