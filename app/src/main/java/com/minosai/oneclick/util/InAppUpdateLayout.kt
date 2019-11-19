package com.minosai.oneclick.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentSender
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task


class InAppUpdateLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val requestCode = 4599
    private val tag = this.javaClass.simpleName

    private var isUpdateAvailable = false

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>

    private val listener = InstallStateUpdatedListener { state ->
        Log.d(tag, "InstallState: $state")
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            requestUserToInstallUpdate()
        }
    }

    private val activity: Activity?
        get() {
            var context = context
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }

    private fun checkForUpdate() {
        appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.d(tag, "checkForUpdate: $appUpdateInfo")

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                showUpdateAnimation()
            }

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // update has been downloaded but not installed yet
                requestUserToInstallUpdate()
            }
        }
    }

    private fun showUpdateAnimation() {
        isUpdateAvailable = true
        visibility = View.VISIBLE
    }

    override fun onClick(v: View) {
        startFlexibleUpdateFlow()
    }

    private fun startFlexibleUpdateFlow() {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfoTask.result,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    requestCode
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
            Log.e(tag, "startFlexibleUpdateFlow: Failed to start update flow", e)
        }

    }

    private fun requestUserToInstallUpdate() {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Update available")
        alertDialog.setMessage("An update is available. Install now?")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES") { _, _ ->
            appUpdateManager.completeUpdate()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        appUpdateManager.unregisterListener(listener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        visibility = View.GONE
        setOnClickListener(this)

        appUpdateManager = AppUpdateManagerFactory.create(context)
        appUpdateManager.registerListener(listener)
        checkForUpdate()
    }

    /**
     * This method is called by enclosing [Activity]. It hides the view
     * if either the update is cancelled or failed.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == this.requestCode) {
            if (resultCode != Activity.RESULT_OK) {
                // update cancelled or failed
                visibility = View.GONE
                appUpdateManager.unregisterListener(listener)
            }
        }
    }
}