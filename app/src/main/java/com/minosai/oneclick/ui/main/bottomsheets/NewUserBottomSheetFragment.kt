package com.minosai.oneclick.ui.main.bottomsheets

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minosai.oneclick.R
import com.minosai.oneclick.util.listener.NewUserSheetListener
import kotlinx.android.synthetic.main.fragment_bottom_sheet_newuser.*

class NewUserBottomSheetFragment() : BottomSheetDialogFragment() {

    lateinit var listener: NewUserSheetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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