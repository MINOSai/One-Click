package com.minosai.oneclick.ui.dialog.bottomsheets

import android.os.Bundle
import androidx.fragment.app.DialogFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?)
            = BottomSheetDialog(requireContext(), theme)

}