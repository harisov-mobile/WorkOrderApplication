package ru.internetcloud.workorderapplication.presentation.workorder.detail.performers

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemPerformerDetailListBinding
import ru.internetcloud.workorderapplication.databinding.ItemPerformerDetailListSelectedBinding
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentListAdapter

class PerformerDetailListAdapter : ListAdapter<PerformerDetail, PerformerDetailListViewHolder>(PerformerDetailDiffCallback()) {

    // для отработки нажатий на элемент списка - переменная, которая будет хранить лямбда-функцию,
    // на вход лямбда-функции в качестве параметра будет передан workOrder: WorkOrder,
    // лямбда-функция ничего не возрващает (то есть Unit)
    // а первоначально переменная содержит null
    var onPerformerDetailClickListener: ((performerDetail: PerformerDetail) -> Unit)? = null

    companion object {
        private const val SELECTED_ITEM_TYPE = 0
        private const val UNSELECTED_ITEM_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerDetailListViewHolder {

        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemPerformerDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemPerformerDetailListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return PerformerDetailListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformerDetailListViewHolder, position: Int) {
        val performerDetail = getItem(position)
        val binding = holder.binding

        if (performerDetail.isSelected) {
            val currentBinding = binding as ItemPerformerDetailListSelectedBinding
            currentBinding.nameTextView.text = performerDetail.employee?.name
            currentBinding.lineNumberTextView.text = performerDetail.lineNumber.toString()
        } else {
            val currentBinding = binding as ItemPerformerDetailListBinding
            currentBinding.nameTextView.text = performerDetail.employee?.name
            currentBinding.lineNumberTextView.text = performerDetail.lineNumber.toString()
        }

        binding.root.setOnClickListener {
            onPerformerDetailClickListener?.invoke(performerDetail)
            notifyItemChanged(currentList.indexOf(performerDetail), Unit) // перерисовка без анимации
        }
    }

    override fun getItemViewType(position: Int): Int {
        val performerDetail = getItem(position)

        return if (performerDetail.isSelected) {
            SELECTED_ITEM_TYPE
        } else {
            UNSELECTED_ITEM_TYPE
        }
    }
}
