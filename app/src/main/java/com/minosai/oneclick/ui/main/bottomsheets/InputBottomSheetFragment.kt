package com.minosai.oneclick.ui.main.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.listener.InputSheetListener
import kotlinx.android.synthetic.main.fragment_bottom_sheet_input.*

class InputBottomSheetFragment : RoundedBottomSheetDialogFragment() {

    lateinit var listener: InputSheetListener
    lateinit var action: Constants.SheetAction

    fun init(listener: InputSheetListener, action: Constants.SheetAction) {
        this.listener = listener
        this.action = action
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return inflater.inflate(R.layout.fragment_bottom_sheet_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(action) {
            Constants.SheetAction.NEW_ACCOUNT -> text_input_sheet.text = "New account"
            Constants.SheetAction.EDIT_ACCOUNT -> text_input_sheet.text = "Edit account"
            Constants.SheetAction.INCOGNITO -> text_input_sheet.text = "Incognito"
        }

        fab_newuser_cancel.setOnClickListener { dismiss() }

        fab_newuser_done.setOnClickListener {
            //TODO: validate inputs
            listener.onSheetResponse(input_newuser_username.text.toString(), input_newuser_password.text.toString(), false, action)
            dismiss()
        }
    }
}