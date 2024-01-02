package ru.internetcloud.workorderapplication.workorderdetail.presentation.jobdetails

import androidx.recyclerview.widget.DiffUtil
import ru.internetcloud.workorderapplication.common.domain.model.document.JobDetail

class JobDetailDiffCallback : DiffUtil.ItemCallback<JobDetail>() {
    override fun areItemsTheSame(oldItem: JobDetail, newItem: JobDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobDetail, newItem: JobDetail): Boolean {
        return oldItem == newItem
    }
}
