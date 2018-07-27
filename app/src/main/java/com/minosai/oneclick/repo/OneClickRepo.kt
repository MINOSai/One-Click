package com.minosai.oneclick.repo

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.model.AccountInfo
import javax.inject.Inject

class OneClickRepo @Inject constructor(val dao: OneClickDao) {

    fun fetchAccounts(): LiveData<PagedList<AccountInfo>> {
        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPrefetchDistance(20)
                .setPageSize(25)
                .build()
        return LivePagedListBuilder(dao.getAllAccounts(), pagedListConfig)
                .build()
    }

}