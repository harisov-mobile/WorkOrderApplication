package ru.internetcloud.workorderapplication.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderListAdapter : RecyclerView.Adapter<WorkOrderListAdapter.WorkOrderViewHolder>() {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возрващает (то есть Unit)
    // а первоначально переменная содержит null
    var onWorkOrderClickListener: ((workOrder: WorkOrder) -> Unit)? = null

    var workOrderList = listOf<WorkOrder>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class WorkOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.tv_name)
        val countTextView = itemView.findViewById<TextView>(R.id.tv_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_work_order_list, parent, false)
        return WorkOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkOrderViewHolder, position: Int) {
        val workOrder = workOrderList[position]
        holder.nameTextView.text = "${workOrder.number} ${workOrder.date}"

        holder.itemView.setOnClickListener {
            onWorkOrderClickListener?.invoke(workOrder)
        }
    }

    override fun getItemCount(): Int {
        return workOrderList.size
    }
}
