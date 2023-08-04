package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListPostedBinding
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder

class WorkOrderListAdapter(
    private val workOrderListListener: WorkOrderListListener
) : ListAdapter<WorkOrder, WorkOrderListViewHolder>(WorkOrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderListViewHolder {
        val binding: ViewBinding
        if (viewType == POSTED_ITEM_TYPE) {
            binding = ItemWorkOrderListPostedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == UNPOSTED_ITEM_TYPE) {
            binding = ItemWorkOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }

        val workOrderListViewHolder = WorkOrderListViewHolder(binding, workOrderListListener)
        binding.root.setOnClickListener(workOrderListViewHolder)

        return workOrderListViewHolder
    }

    override fun onBindViewHolder(holder: WorkOrderListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).posted) {
            POSTED_ITEM_TYPE
        } else {
            UNPOSTED_ITEM_TYPE
        }
    }

    companion object {
        private const val POSTED_ITEM_TYPE = 1
        private const val UNPOSTED_ITEM_TYPE = 0
    }
}
