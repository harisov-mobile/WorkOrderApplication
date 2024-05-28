package ru.internetcloud.workorderapplication.workorders.presentation

import androidx.recyclerview.widget.DiffUtil
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder

class WorkOrderDiffCallback : DiffUtil.ItemCallback<WorkOrder>() {
    override fun areItemsTheSame(oldItem: WorkOrder, newItem: WorkOrder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WorkOrder, newItem: WorkOrder): Boolean {
        return oldItem == newItem
    }
}
