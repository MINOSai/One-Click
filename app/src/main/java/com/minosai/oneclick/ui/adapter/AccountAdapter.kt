package com.minosai.oneclick.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.util.*
import kotlinx.android.synthetic.main.item_account_layout.view.*

class AccountAdapter(
        private val listener: (Constants.AccountAction, AccountInfo) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    companion object {
        var accountList: List<AccountInfo>? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            AccountViewHolder(parent.inflate(R.layout.item_account_layout))

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        accountList?.get(position).let {
            holder.bind(it, listener)
        }
    }

    override fun getItemCount() = accountList?.size ?: 0

    fun updateList(accounts: List<AccountInfo>?) {
        accountList = accounts
        notifyDataSetChanged()
    }

    class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
                account: AccountInfo?,
                listener: (Constants.AccountAction, AccountInfo) -> Unit
        ) = with(itemView) {
            account?.let { accountInfo ->

                text_account_id.text = accountInfo.username

                if (accountInfo.isActiveAccount) {
                    layout_account_actions.show()
                    text_account_primary.show()
                    button_account_make_primary.hide()
                    fab_account_delete.hide()

                } else {
                    layout_account_actions.hide()
                    text_account_primary.hide()
                    button_account_make_primary.show()
                    fab_account_delete.show()
                }

                card_account_container.setOnClickListener {
                    layout_account_actions.toggleVisibility()
                }

                button_account_make_primary.setOnClickListener {
                    listener(Constants.AccountAction.SET_PRIMARY, accountInfo)
                }

                button_account_login.setOnClickListener {
                    listener(Constants.AccountAction.LOGIN, accountInfo)
                }

                fab_account_password_copy.setOnClickListener {
                    listener(Constants.AccountAction.COPY_PASSWORD, accountInfo)
                }

                fab_account_password_view.setOnClickListener {
                    listener(Constants.AccountAction.VIEW_PASSWORD, accountInfo)
                }

                fab_account_edit.setOnClickListener {
                    listener(Constants.AccountAction.EDIT_ACCOUNT, accountInfo)
                }

                fab_account_delete.setOnClickListener {
                    listener(Constants.AccountAction.DELETE_ACCOUNT, accountInfo)
                }
            }
        }
    }
}