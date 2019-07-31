package com.minosai.oneclick.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minosai.oneclick.R
import com.minosai.oneclick.util.inflate
import kotlinx.android.synthetic.main.item_info_bullet.view.*

class InfoBulletAdapter(val items: List<String>) :
        RecyclerView.Adapter<InfoBulletAdapter.InfoBulletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            InfoBulletViewHolder(parent.inflate(R.layout.item_info_bullet))

    override fun onBindViewHolder(holder: InfoBulletViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class InfoBulletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(content: String) = with(itemView) {
            info_bullet_text.text = content
        }
    }
}