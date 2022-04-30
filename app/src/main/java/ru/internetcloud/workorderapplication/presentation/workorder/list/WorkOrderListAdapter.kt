package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListPostedBinding
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderListAdapter : ListAdapter<WorkOrder, WorkOrderListViewHolder>(WorkOrderDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возвращает (то есть Unit)
    // а первоначально переменная содержит null
    var onWorkOrderClickListener: ((workOrder: WorkOrder) -> Unit)? = null

    companion object {
        private const val PERFORMERS_STRING_SIZE = 50
        private const val POSTED_ITEM_TYPE = 1
        private const val UNPOSTED_ITEM_TYPE = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOrderListViewHolder {

        var binding: ViewBinding
        if (viewType == POSTED_ITEM_TYPE) {
            binding = ItemWorkOrderListPostedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == UNPOSTED_ITEM_TYPE) {
            binding = ItemWorkOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return WorkOrderListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkOrderListViewHolder, position: Int) {
        val workOrder = getItem(position)
        val binding = holder.binding

        if (workOrder.posted) {
            val currentBinding = binding as ItemWorkOrderListPostedBinding
            currentBinding.numberTextView.text = workOrder.number
            currentBinding.dateTextView.text = DateConverter.getDateString(workOrder.date)
            currentBinding.partnerTextView.text = workOrder.partner?.name
            currentBinding.carTextView.text = workOrder.car?.name

            currentBinding.departmentTextView.text = workOrder.department?.name

            var performersString = workOrder.performersString
            if (performersString.length > PERFORMERS_STRING_SIZE) {
                performersString = performersString.substring(0, PERFORMERS_STRING_SIZE) + "..."
            }
            currentBinding.performersStringTextView.text = performersString
            currentBinding.commentTextView.text = workOrder.comment
            currentBinding.requestReasonTextView.text = workOrder.requestReason
        } else {
            val currentBinding = binding as ItemWorkOrderListBinding
            currentBinding.numberTextView.text = workOrder.number
            currentBinding.dateTextView.text = DateConverter.getDateString(workOrder.date)
            currentBinding.partnerTextView.text = workOrder.partner?.name
            currentBinding.carTextView.text = workOrder.car?.name

            currentBinding.departmentTextView.text = workOrder.department?.name

            var performersString = workOrder.performersString
            if (performersString.length > PERFORMERS_STRING_SIZE) {
                performersString = performersString.substring(0, PERFORMERS_STRING_SIZE) + "..."
            }
            currentBinding.performersStringTextView.text = performersString
            currentBinding.commentTextView.text = workOrder.comment
            currentBinding.requestReasonTextView.text = workOrder.requestReason
        }

        binding.root.setOnClickListener {
            onWorkOrderClickListener?.invoke(workOrder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val workOrder = getItem(position)

        return if (workOrder.posted) {
            POSTED_ITEM_TYPE
        } else {
            UNPOSTED_ITEM_TYPE
        }
    }
}
