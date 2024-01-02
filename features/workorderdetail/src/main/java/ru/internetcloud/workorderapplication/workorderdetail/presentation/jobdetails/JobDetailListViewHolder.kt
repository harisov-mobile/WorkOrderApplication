package ru.internetcloud.workorderapplication.workorderdetail.presentation.jobdetails

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.common.domain.model.document.JobDetail
import ru.internetcloud.workorderapplication.workorderdetail.R
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemJobDetailListBinding
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemJobDetailListSelectedBinding

class JobDetailListViewHolder(
    private val binding: ViewBinding,
    private val jobDetailListListener: JobDetailListListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var jobDetail: JobDetail

    fun bind(jobDetail: JobDetail, viewType: Int) {
        this.jobDetail = jobDetail

        when (viewType) {
            JobDetailListAdapter.SELECTED_ITEM_TYPE -> {
                val currentBinding = binding as ItemJobDetailListSelectedBinding
                val currentContext = currentBinding.root.context
                currentBinding.carJobTextView.text = jobDetail.carJob?.name
                currentBinding.workingHourTextView.text =
                    currentContext.getString(R.string.job_detail_working_hour, jobDetail.workingHour?.name)
                currentBinding.quantityTextView.text =
                    currentContext.getString(R.string.job_detail_quantity, jobDetail.quantity.toString())
                currentBinding.timeNormTextView.text =
                    currentContext.getString(R.string.job_detail_time_norm, jobDetail.timeNorm.toString())
                currentBinding.sumTextView.text =
                    currentContext.getString(R.string.job_detail_sum, jobDetail.sum.toString())
                currentBinding.lineNumberTextView.text =
                    currentContext.getString(R.string.job_detail_line_number, jobDetail.lineNumber.toString())
            }

            JobDetailListAdapter.UNSELECTED_ITEM_TYPE -> {
                val currentBinding = binding as ItemJobDetailListBinding
                val currentContext = currentBinding.root.context
                currentBinding.carJobTextView.text = jobDetail.carJob?.name
                currentBinding.workingHourTextView.text =
                    currentContext.getString(R.string.job_detail_working_hour, jobDetail.workingHour?.name)
                currentBinding.quantityTextView.text =
                    currentContext.getString(R.string.job_detail_quantity, jobDetail.quantity.toString())
                currentBinding.timeNormTextView.text =
                    currentContext.getString(R.string.job_detail_time_norm, jobDetail.timeNorm.toString())
                currentBinding.sumTextView.text =
                    currentContext.getString(R.string.job_detail_sum, jobDetail.sum.toString())
                currentBinding.lineNumberTextView.text =
                    currentContext.getString(R.string.job_detail_line_number, jobDetail.lineNumber.toString())
            }

            else -> error("Unknown viewType = $viewType")
        }
    }

    override fun onClick(v: View?) {
        jobDetailListListener.onClickJobDetail(jobDetail)
    }
}
