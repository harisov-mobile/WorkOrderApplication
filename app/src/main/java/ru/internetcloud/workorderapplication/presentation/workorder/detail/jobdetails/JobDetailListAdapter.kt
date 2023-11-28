package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.internetcloud.workorderapplication.databinding.ItemJobDetailListBinding
import ru.internetcloud.workorderapplication.databinding.ItemJobDetailListSelectedBinding
import ru.internetcloud.workorderapplication.domain.model.document.JobDetail

class JobDetailListAdapter(
    private val jobDetailListListener: JobDetailListListener
) : ListAdapter<JobDetail, JobDetailListViewHolder>(JobDetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobDetailListViewHolder {
        lateinit var jobDetailListViewHolder: JobDetailListViewHolder

        val binding = when(viewType) {
            UNSELECTED_ITEM_TYPE -> {
                ItemJobDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            SELECTED_ITEM_TYPE -> {
                ItemJobDetailListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            else -> error("Unknown viewType : $viewType")
        }

        jobDetailListViewHolder = JobDetailListViewHolder(binding, jobDetailListListener)

        // назначаем обработчик кликов для всего элемента списка:
        binding.root.setOnClickListener(jobDetailListViewHolder)

        return jobDetailListViewHolder
    }

    override fun onBindViewHolder(holder: JobDetailListViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position))
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
        const val SELECTED_ITEM_TYPE = 0
        const val UNSELECTED_ITEM_TYPE = 1
    }
}
