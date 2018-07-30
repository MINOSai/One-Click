package com.minosai.oneclick.ui.fragment.main

import android.arch.lifecycle.ViewModel
import com.minosai.oneclick.repo.OneClickRepo
import javax.inject.Inject

class MainViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    var displayName: String = repo.getDisplayName()

}