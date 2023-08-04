package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkOrderListPostedBinding
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder

class WorkOrderListViewHolder(
    private val binding: ViewBinding,
    private val workOrderListListener: WorkOrderListListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var workOrder: WorkOrder

    fun bind(workOrder: WorkOrder) {
        this.workOrder = workOrder

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
    }

    override fun onClick(v: View?) {
        workOrderListListener.onItemClick(workOrder)
    }

    companion object {
        private const val PERFORMERS_STRING_SIZE = 50
    }
}
