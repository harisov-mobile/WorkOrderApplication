package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListBinding
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderListAdapter : ListAdapter<WorkOrder, WorkOrderListViewHolder>(WorkOrderDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возрващает (то есть Unit)
    // а первоначально переменная содержит null
    var onWorkOrderClickListener: ((workOrder: WorkOrder) -> Unit)? = null

    companion object {
        private const val PERFORMERS_STRING_SIZE = 50
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderListViewHolder {

        val binding = ItemWorkOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkOrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkOrderListViewHolder, position: Int) {
        val workOrder = getItem(position)
        val binding = holder.binding

        binding.numberTextView.text = workOrder.number
        binding.dateTextView.text = DateConverter.getDateString(workOrder.date)
        binding.partnerTextView.text = workOrder.partner?.name
        binding.carTextView.text = workOrder.car?.name

        binding.departmentTextView.text = workOrder.department?.name

        var performersString = workOrder.performersString
        if (performersString.length > PERFORMERS_STRING_SIZE) {
            performersString = performersString.substring(0, PERFORMERS_STRING_SIZE) + "..."
        }
        binding.performersStringTextView.text = performersString
        binding.commentTextView.text = workOrder.comment
        binding.requestReasonTextView.text = workOrder.requestReason

        binding.root.setOnClickListener {
            onWorkOrderClickListener?.invoke(workOrder)
        }
    }
}
