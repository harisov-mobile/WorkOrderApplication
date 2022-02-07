package ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemRepairTypeListBinding
import ru.internetcloud.workorderapplication.databinding.ItemRepairTypeListSelectedBinding
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

class RepairTypeListAdapter(var repairTypes: List<RepairType>) : RecyclerView.Adapter<RepairTypeListViewHolder>() {
    var onRepairTypeClickListener: ((repairType: RepairType) -> Unit)? = null
    var onRepairTypeLongClickListener: ((repairType: RepairType) -> Unit)? = null

    companion object {
        private const val SELECTED_ITEM_TYPE = 0
        private const val UNSELECTED_ITEM_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepairTypeListViewHolder {

        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemRepairTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemRepairTypeListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return RepairTypeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepairTypeListViewHolder, position: Int) {
        val repairType = repairTypes[position]

        val binding = holder.binding

        if (repairType.isSelected) {
            val currentBinding = binding as ItemRepairTypeListSelectedBinding
            currentBinding.nameTextView.text = repairType.name
        } else {
            val currentBinding = binding as ItemRepairTypeListBinding
            currentBinding.nameTextView.text = repairType.name
        }

        binding.root.setOnClickListener {
            onRepairTypeClickListener?.invoke(repairType)
            notifyItemChanged(repairTypes.indexOf(repairType), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onRepairTypeLongClickListener?.invoke(repairType)
            true
        }
    }

    override fun getItemCount(): Int {
        return repairTypes.size
    }

    override fun getItemViewType(position: Int): Int {
        val repairType = repairTypes[position]

        return if (repairType.isSelected) {
            SELECTED_ITEM_TYPE
        } else {
            UNSELECTED_ITEM_TYPE
        }
    }
}
