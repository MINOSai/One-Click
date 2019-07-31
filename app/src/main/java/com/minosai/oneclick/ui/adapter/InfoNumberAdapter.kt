package com.minosai.oneclick.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minosai.oneclick.R
import com.minosai.oneclick.util.inflate
import kotlinx.android.synthetic.main.item_info_number.view.*

class InfoNumberAdapter(val items: List<String>) :
        RecyclerView.Adapter<InfoNumberAdapter.InfoNumberViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            InfoNumberViewHolder(parent.inflate(R.layout.item_info_number))

    override fun onBindViewHolder(holder: InfoNumberViewHolder, position: Int) {
        holder.bind(position + 1, items[position])
    }

    override fun getItemCount() = items.size

    class InfoNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int, content: String) = with(itemView) {
            info_number_position.text = position.toString()
            info_number_text.text = content
        }
    }
}