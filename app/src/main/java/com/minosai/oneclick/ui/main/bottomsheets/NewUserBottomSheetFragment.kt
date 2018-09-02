package com.minosai.oneclick.ui.main.bottomsheets

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.minosai.oneclick.R
import com.minosai.oneclick.util.listener.NewUserSheetListener
import kotlinx.android.synthetic.main.fragment_bottom_sheet_newuser.*

class NewUserBottomSheetFragment() : BottomSheetDialogFragment() {

    lateinit var listener: NewUserSheetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return inflater.inflate(R.layout.fragment_bottom_sheet_newuser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_newuser_cancel.setOnClickListener { dismiss() }

        button_newuser_login.setOnClickListener {
            listener.onAddNewUser(input_newuser_username.text.toString(), input_newuser_password.text.toString(), false)
            dismiss()
        }
    }
}