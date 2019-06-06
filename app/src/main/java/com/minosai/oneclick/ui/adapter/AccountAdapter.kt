package com.minosai.oneclick.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.ui.main.MainViewModel
import com.minosai.oneclick.util.hide
import com.minosai.oneclick.util.inflate
import com.minosai.oneclick.util.show
import com.minosai.oneclick.util.toggleVisibility
import kotlinx.android.synthetic.main.item_account_layout.view.*

class AccountAdapter(
        private val context: Context?,
        private val mainViewModel: MainViewModel,
        private val listener: (AccountInfo) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    companion object {
        var accountList: List<AccountInfo>? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AccountViewHolder(parent.inflate(R.layout.item_account_layout))

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

                text_account_id.text = accountInfo.username

//                if (accountInfo.usage.isBlank()) {
//                    text_account_usage_placeholder.show()
//                    text_account_usage.hide()
//                    text_account_date.hide()
//                } else {
//                    text_account_usage_placeholder.hide()
//                    text_account_usage.show()
//                    text_account_date.show()
//                    text_account_date.text = "18th Aug 2019"
//                    text_account_usage.text = accountInfo.usage
//                }

//                if (AccountAdapter.accountList?.size == 1) {
//                    chip_account_delete.hide()
//                }

                if (accountInfo.isActiveAccount) {
                    layout_account_actions.show()
                    text_account_primary.show()
                    button_account_make_primary.hide()
                    chip_account_delete.hide()

                } else {
                    layout_account_actions.hide()
                    text_account_primary.hide()
                    button_account_make_primary.show()
                    chip_account_delete.show()
                }

                card_account_container.setOnClickListener {
                    layout_account_actions.toggleVisibility()
                }

                button_account_make_primary.setOnClickListener {
                    mainViewModel.setPrimaryAccount(accountInfo.username)
                }

                chip_account_delete.setOnClickListener {
                    mainViewModel.removeAccount(accountInfo)
                }

                chip_account_view_password.setOnClickListener {
                    val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Account Info", "Username: ${accountInfo.username}\nPassword: ${accountInfo.password}")
                    clipboard.primaryClip = clip
//                    snackbar(mainViewModel.view, "Account details copied to clipboard")
                    Snackbar.make(mainViewModel.view, "Account details copied to clipboard", Snackbar.LENGTH_SHORT).show()

                }

                chip_account_edit.setOnClickListener {
                    listener(accountInfo)
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