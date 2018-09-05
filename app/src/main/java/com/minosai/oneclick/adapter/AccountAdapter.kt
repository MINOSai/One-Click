package com.minosai.oneclick.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.ui.main.MainViewModel
import com.minosai.oneclick.util.*
import kotlinx.android.synthetic.main.account_row_layout.view.*

class AccountAdapter(
        private val context: Context?,
        private val mainViewModel: MainViewModel,
        private val listener: (AccountInfo) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private var accountList: List<AccountInfo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AccountViewHolder(parent.inflate(R.layout.account_row_layout))

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        accountList?.get(position).let {
            holder.bind(it, listener, mainViewModel, context)
        }
    }

    override fun getItemCount() = accountList?.size ?: 0

    fun updateList(accounts: List<AccountInfo>?) {
        accountList = accounts
        notifyDataSetChanged()
    }

    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(account: AccountInfo?, listener: (AccountInfo) -> Unit, mainViewModel: MainViewModel, context: Context?) = with(itemView) {
            // TODO: setup views
            account?.let { accountInfo ->

                account_item_username.text = accountInfo.username

                if (accountInfo.usage.isBlank()) {
                    account_item_empty_state.show()
                    account_item_details_layout.hide()
                } else {
                    account_item_empty_state.hide()
                    account_item_details_layout.show()
//                    account_item_date.text = accountInfo.renewalDate
                    account_item_date.text = "18th Aug 2019"
                    account_item_usage.text = accountInfo.usage
                }

                if (accountInfo.isActiveAccount) {
                    card_account_item.setBackgroundTint(R.color.colorActiveAccount)
//                    account_item_primary_linearlayout.setPadding(
//                            dpToPixels(8, context), 0, 0, 0
//                    )
                    account_item_actions_layout.visibility = View.VISIBLE
                    accounnt_item_makeprimary_layout.visibility = View.GONE
                    account_item_toggle_button.visibility = View.GONE
                } else {
                    card_account_item.setBackgroundTint(android.R.color.white)
                    account_item_primary_linearlayout.setPadding(
                            dpToPixels(0, context), 0, 0, 0
                    )
                    account_item_actions_layout.visibility = View.GONE
                    accounnt_item_makeprimary_layout.visibility = View.VISIBLE
                    account_item_toggle_button.visibility = View.VISIBLE
                }

                account_item_toggle_button.setOnClickListener {
                    account_item_actions_layout.toggleVisibility(account_item_toggle_button)
                }

                account_item_action_primary.setOnClickListener {
                    mainViewModel.setPrimaryAccount(accountInfo.username)
                }

                account_item_action_delete.setOnClickListener {
                    mainViewModel.removeAccount(accountInfo)
                }
            }
        }

        private fun dpToPixels(sizeInDp: Int, context: Context?): Int {
            //FIXME: This breaks the app
            var dpAsPixels = (0.5f).toInt()
            context?.let {
                val scale = context.resources.displayMetrics.density
                dpAsPixels = (sizeInDp * scale + 0.5f).toInt()
            }
            return dpAsPixels
        }
    }
}