package ru.internetcloud.workorderapplication.workorderdetail.presentation.performers

import androidx.recyclerview.widget.DiffUtil
import ru.internetcloud.workorderapplication.common.domain.model.document.PerformerDetail

class PerformerDetailDiffCallback : DiffUtil.ItemCallback<PerformerDetail>() {
    override fun areItemsTheSame(oldItem: PerformerDetail, newItem: PerformerDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PerformerDetail, newItem: PerformerDetail): Boolean {
        return oldItem == newItem
    }
}
