package com.minosai.oneclick.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.util.hide
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.show
import kotlinx.android.synthetic.main.layout_dialog_oneclick.*

class OneClickDialogClass(
        context: Context,
        val webService: WebService,
        val accountInfo: AccountInfo
) : Dialog(context), LoginLogoutListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_dialog_oneclick)

        text_dialog_acc_username.text = accountInfo.username

        text_dialog_status.hide()

        button_dialog_login.setOnClickListener {
            it.startAnimation(getAnimation())
            webService.login(this, accountInfo.username, accountInfo.password)
        }
        button_dialog_logout.setOnClickListener {
            it.startAnimation(getAnimation())
            webService.logout(this)
        }

        image_dialog_close.setOnClickListener {
            dismiss()
        }
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean, responseString: String) {
        text_dialog_status.show()
        button_dialog_login.clearAnimation()
        button_dialog_logout.clearAnimation()

        when (requestType) {
            WebService.Companion.RequestType.LOGIN -> {
                if (isLogged) {
                    showSuccess()
                } else {
                    showFailure()
                }
            }
            WebService.Companion.RequestType.LOGOUT -> {
                if (isLogged) {
                    showSuccess()
                } else {
                    showFailure()
                }
            }
        }
    }

    private fun getAnimation() = AlphaAnimation(1f, 0.5f).apply {
        duration = 500
        interpolator = LinearInterpolator()
        repeatCount = Animation.INFINITE
        repeatMode = Animation.REVERSE
    }

    private fun showSuccess() {
        text_dialog_status.text = "Success"
        text_dialog_status.setTextColor(context.resources.getColor(android.R.color.holo_green_light))

//        Handler().postDelayed({
//            dismiss()
//        }, 1500)
    }

    private fun showFailure() {
        text_dialog_status.text = "Failed"
        text_dialog_status.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
    }

}