package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.ItemJobDetailListBinding
import ru.internetcloud.workorderapplication.databinding.ItemJobDetailListSelectedBinding
import ru.internetcloud.workorderapplication.domain.document.JobDetail

class JobDetailListAdapter : ListAdapter<JobDetail, JobDetailListViewHolder>(JobDetailDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан jobDetail: JobDetail,
    // а первоначально переменная содержит null
    var onJobDetailClickListener: ((jobDetail: JobDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobDetailListViewHolder {
        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemJobDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemJobDetailListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return JobDetailListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobDetailListViewHolder, position: Int) {
        val jobDetail = getItem(position)
        val binding = holder.binding

        if (jobDetail.isSelected) {
            val currentBinding = binding as ItemJobDetailListSelectedBinding
            val currentContext = currentBinding.root.context
            currentBinding.carJobTextView.text = jobDetail.carJob?.name
            currentBinding.workingHourTextView.text = currentContext.getString(R.string.job_detail_working_hour, jobDetail.workingHour?.name)
            currentBinding.quantityTextView.text = currentContext.getString(R.string.job_detail_quantity, jobDetail.quantity.toString())
            currentBinding.timeNormTextView.text = currentContext.getString(R.string.job_detail_time_norm, jobDetail.timeNorm.toString())
            currentBinding.sumTextView.text = currentContext.getString(R.string.job_detail_sum, jobDetail.sum.toString())
            currentBinding.lineNumberTextView.text = currentContext.getString(R.string.job_detail_line_number, jobDetail.lineNumber.toString())
        } else {
            val currentBinding = binding as ItemJobDetailListBinding
            val currentContext = currentBinding.root.context
            currentBinding.carJobTextView.text = jobDetail.carJob?.name
            currentBinding.workingHourTextView.text = currentContext.getString(R.string.job_detail_working_hour, jobDetail.workingHour?.name)
            currentBinding.quantityTextView.text = currentContext.getString(R.string.job_detail_quantity, jobDetail.quantity.toString())
            currentBinding.timeNormTextView.text = currentContext.getString(R.string.job_detail_time_norm, jobDetail.timeNorm.toString())
            currentBinding.sumTextView.text = currentContext.getString(R.string.job_detail_sum, jobDetail.sum.toString())
            currentBinding.lineNumberTextView.text = currentContext.getString(R.string.job_detail_line_number, jobDetail.lineNumber.toString())
        }

        binding.root.setOnClickListener {
            onJobDetailClickListener?.invoke(jobDetail)
            notifyItemChanged(currentList.indexOf(jobDetail), Unit) // перерисовка без анимации
        }
    }

    override fun getItemViewType(position: Int): Int {
        val jobDetail = getItem(position)

        return if (jobDetail.isSelected) {
            SELECTED_ITEM_TYPE
        } else {
            UNSELECTED_ITEM_TYPE
        }
    }

    companion object {
        private const val SELECTED_ITEM_TYPE = 0
        private const val UNSELECTED_ITEM_TYPE = 1
    }
}
