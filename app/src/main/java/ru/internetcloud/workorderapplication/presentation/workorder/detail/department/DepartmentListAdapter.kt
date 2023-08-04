package ru.internetcloud.workorderapplication.presentation.workorder.detail.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemDepartmentListBinding
import ru.internetcloud.workorderapplication.databinding.ItemDepartmentListSelectedBinding
import ru.internetcloud.workorderapplication.domain.model.catalog.Department

class DepartmentListAdapter(var departments: List<Department>) : RecyclerView.Adapter<DepartmentListViewHolder>() {
    var onDepartmentClickListener: ((department: Department) -> Unit)? = null
    var onDepartmentLongClickListener: ((department: Department) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentListViewHolder {
        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemDepartmentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemDepartmentListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return DepartmentListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepartmentListViewHolder, position: Int) {
        val department = departments[position]

        val binding = holder.binding

        if (department.isSelected) {
            val currentBinding = binding as ItemDepartmentListSelectedBinding
            currentBinding.nameTextView.text = department.name
        } else {
            val currentBinding = binding as ItemDepartmentListBinding
            currentBinding.nameTextView.text = department.name
        }

        binding.root.setOnClickListener {
            onDepartmentClickListener?.invoke(department)
            notifyItemChanged(departments.indexOf(department), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onDepartmentLongClickListener?.invoke(department)
            true
        }
    }

    override fun getItemCount(): Int {
        return departments.size
    }

    override fun getItemViewType(position: Int): Int {
        val department = departments[position]

        return if (department.isSelected) {
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
