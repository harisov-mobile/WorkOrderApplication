package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListBinding
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderListAdapter : ListAdapter<WorkOrder, WorkOrderListViewHolder>(WorkOrderDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возрващает (то есть Unit)
    // а первоначально переменная содержит null
    var onWorkOrderClickListener: ((workOrder: WorkOrder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderListViewHolder {

        val binding = ItemWorkOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkOrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkOrderListViewHolder, position: Int) {
        val workOrder = getItem(position)
        val binding = holder.binding
        // binding.tvName.text = "${workOrder.number} ${workOrder.date}"
        binding.numberTextView.text = workOrder.number
        binding.dateTextView.text = DateConverter.getDateString(workOrder.date)
        binding.carTextView.text = workOrder.car?.name

        binding.departmentTextView.text = workOrder.department?.name
        // binding.commentTextView.text = workOrder.comment
        binding.requestReasonTextView.text = workOrder.requestReason

        binding.commentTextView.text = workOrder.jobDetails.toString()

        binding.root.setOnClickListener {
            onWorkOrderClickListener?.invoke(workOrder)
        }
    }
}
