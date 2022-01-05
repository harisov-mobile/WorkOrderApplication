package ru.internetcloud.workorderapplication.presentation.workorder.detail.performers

import androidx.recyclerview.widget.DiffUtil
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail

class PerformerDetailDiffCallback : DiffUtil.ItemCallback<PerformerDetail>() {
    override fun areItemsTheSame(oldItem: PerformerDetail, newItem: PerformerDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PerformerDetail, newItem: PerformerDetail): Boolean {
        return oldItem == newItem
    }
}
