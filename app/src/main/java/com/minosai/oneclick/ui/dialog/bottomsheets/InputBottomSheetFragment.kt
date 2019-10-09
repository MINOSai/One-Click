package com.minosai.oneclick.ui.dialog.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.hideKeyboard
import com.minosai.oneclick.util.listener.InputSheetListener
import kotlinx.android.synthetic.main.fragment_bottom_sheet_input.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_input.view.*

class InputBottomSheetFragment : RoundedBottomSheetDialogFragment() {

    var listener: InputSheetListener? = null
    lateinit var action: Constants.SheetAction
    private var accountInfo: AccountInfo? = null

    fun init(listener: InputSheetListener, action: Constants.SheetAction, accountInfo: AccountInfo?) {
        this.listener = listener
        this.action = action
        this.accountInfo = accountInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_input, container, false)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        when (action) {
            Constants.SheetAction.NEW_ACCOUNT -> {
                view.text_input_sheet.text = "New account"
                view.button_newuser_done.text = "Add"
            }
            Constants.SheetAction.INCOGNITO -> {
                view.text_input_sheet.text = "Incognito"
                view.button_newuser_done.text = "Login"
            }
            Constants.SheetAction.EDIT_ACCOUNT -> {
                view.text_input_sheet.text = "Edit account"
                view.button_newuser_done.text = "Save"
                view.input_newuser_username.editText?.setText(accountInfo?.username)
                view.input_newuser_password.editText?.setText(accountInfo?.password)
            }
        }

        view.button_newuser_cancel.setOnClickListener {
            hideKeyboard()
            dismiss()
        }

        view.button_newuser_done.setOnClickListener {
            val userName = view.input_newuser_username.editText?.text.toString()
            val password = view.input_newuser_password.editText?.text.toString()

            if (isValidInput(view, userName, password)) {
                listener?.onSheetResponse(userName, password,
                        false, action, accountInfo
                )
                view.input_newuser_username.editText?.setText("")
                view.input_newuser_password.editText?.setText("")
                hideKeyboard()
                dismiss()
            }
        }

        return view
    }

    private fun isValidInput(view: View, userName: String, password: String): Boolean {
        var valid = true

        if (userName.isBlank()) {
            view.input_newuser_username.error = "Cannot be blank"
            valid = false
        } else {
            view.input_newuser_username.error = null
        }

        if (password.isBlank()) {
            view.input_newuser_password.error = "Cannot be blank"
            valid = false
        } else {
            view.input_newuser_password.error = null
        }

        return valid
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (action == Constants.SheetAction.EDIT_ACCOUNT && input_newuser_username.editText?.text.isNullOrEmpty()) {
            input_newuser_username.editText?.setText(accountInfo?.username)
            input_newuser_password.editText?.setText(accountInfo?.password)
        }
    }

    override fun onDestroyView() {
        accountInfo = null
        listener = null
        view?.input_newuser_username?.editText?.setText("")
        view?.input_newuser_password?.editText?.setText("")
        super.onDestroyView()
    }
}