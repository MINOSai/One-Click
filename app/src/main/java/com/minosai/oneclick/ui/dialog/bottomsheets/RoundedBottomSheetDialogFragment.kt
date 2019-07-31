package com.minosai.oneclick.ui.dialog.bottomsheets

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper
import com.minosai.oneclick.util.helper.PreferenceHelper.get

open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        val preferences = PreferenceHelper.defaultPrefs(
                requireContext().applicationContext
        )

        val isDarkTheme = preferences[Constants.PREF_DARK_THEME, false] ?: false

        return if (isDarkTheme) {
            R.style.BottomSheetDialogTheme_Dark
        } else {
            R.style.BottomSheetDialogTheme_Light
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

}