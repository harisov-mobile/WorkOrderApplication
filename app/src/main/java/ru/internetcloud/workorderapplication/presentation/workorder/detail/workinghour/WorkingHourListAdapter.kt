package ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkingHourListBinding
import ru.internetcloud.workorderapplication.databinding.ItemWorkingHourListSelectedBinding
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

class WorkingHourListAdapter(var workingHours: List<WorkingHour>) : RecyclerView.Adapter<WorkingHourListViewHolder>() {
    var onWorkingHourClickListener: ((carJob: WorkingHour) -> Unit)? = null
    var onWorkingHourLongClickListener: ((carJob: WorkingHour) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkingHourListViewHolder {
        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemWorkingHourListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemWorkingHourListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return WorkingHourListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkingHourListViewHolder, position: Int) {
        val workingHour = workingHours[position]

        val binding = holder.binding

        if (workingHour.isSelected) {
            val currentBinding = binding as ItemWorkingHourListSelectedBinding
            currentBinding.nameTextView.text = workingHour.name
            currentBinding.priceTextView.text = workingHour.price.toString()
        } else {
            val currentBinding = binding as ItemWorkingHourListBinding
            currentBinding.nameTextView.text = workingHour.name
            currentBinding.priceTextView.text = workingHour.price.toString()
        }

        binding.root.setOnClickListener {
            onWorkingHourClickListener?.invoke(workingHour)
            notifyItemChanged(workingHours.indexOf(workingHour), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onWorkingHourLongClickListener?.invoke(workingHour)
            true
        }
    }

    override fun getItemCount(): Int {
        return workingHours.size
    }

    override fun getItemViewType(position: Int): Int {
        val workingHour = workingHours[position]

        return if (workingHour.isSelected) {
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
