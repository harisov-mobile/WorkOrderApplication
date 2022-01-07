package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

import androidx.recyclerview.widget.DiffUtil
import ru.internetcloud.workorderapplication.domain.document.JobDetail

class JobDetailDiffCallback : DiffUtil.ItemCallback<JobDetail>() {
    override fun areItemsTheSame(oldItem: JobDetail, newItem: JobDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobDetail, newItem: JobDetail): Boolean {
        return oldItem == newItem
    }
}
