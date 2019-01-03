package com.minosai.oneclick.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import com.minosai.oneclick.R
import kotlinx.android.synthetic.main.layout_dialog_oneclick.*

class CustomDialogClass(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_dialog_oneclick)
        setTitle("OneClick")

        button_dialog_logout.setOnClickListener {
            Toast.makeText(context, "logout", Toast.LENGTH_SHORT).show()
        }
        button_dialog_login.setOnClickListener {
            Toast.makeText(context, "login", Toast.LENGTH_SHORT).show()
        }
        image_dialog_close.setOnClickListener {
            dismiss()
        }
    }

}