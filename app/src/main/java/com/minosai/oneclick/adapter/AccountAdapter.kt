package com.minosai.oneclick.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.ui.main.MainViewModel
import com.minosai.oneclick.util.inflate
import kotlinx.android.synthetic.main.account_row_layout.view.*

class AccountAdapter(
        private val listener: (AccountInfo) -> Unit,
        private val context: Context,
        private val mainViewModel: MainViewModel
) : PagedListAdapter<AccountInfo, AccountAdapter.AccountViewHolder>(
        object : DiffUtil.ItemCallback<AccountInfo>() {

            override fun areItemsTheSame(oldItem: AccountInfo?, newItem: AccountInfo?) = oldItem?.username ==newItem?.username

            override fun areContentsTheSame(oldItem: AccountInfo?, newItem: AccountInfo?) = oldItem == newItem
        }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AccountViewHolder(parent.inflate(R.layout.account_row_layout))

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, listener)
        }
    }

    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(account: AccountInfo, listener: (AccountInfo) -> Unit) = with(itemView) {
            // TODO: setup views
            with(account) {
                account_item_username.text = username
                account_item_date.text = renewalDate
                account_item_usage.text = usage
            }
            account_item_action_primary.setOnClickListener {  }
        }
    }
}