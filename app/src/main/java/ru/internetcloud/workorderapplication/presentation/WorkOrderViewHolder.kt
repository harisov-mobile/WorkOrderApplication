package ru.internetcloud.workorderapplication.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R

class WorkOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView = itemView.findViewById<TextView>(R.id.tv_name)
    val countTextView = itemView.findViewById<TextView>(R.id.tv_count)
}
