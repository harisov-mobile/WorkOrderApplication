package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderListAdapter : ListAdapter<WorkOrder, WorkOrderViewHolder>(WorkOrderDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возрващает (то есть Unit)
    // а первоначально переменная содержит null
    var onWorkOrderClickListener: ((workOrder: WorkOrder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_work_order_list, parent, false)
        return WorkOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkOrderViewHolder, position: Int) {
        val workOrder = getItem(position)
        holder.nameTextView.text = "${workOrder.number} ${workOrder.date}"

        holder.itemView.setOnClickListener {
            onWorkOrderClickListener?.invoke(workOrder)
        }
    }
}
