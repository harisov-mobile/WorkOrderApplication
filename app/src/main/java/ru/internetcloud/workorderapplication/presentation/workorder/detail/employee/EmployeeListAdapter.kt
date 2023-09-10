package ru.internetcloud.workorderapplication.presentation.workorder.detail.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemEmployeeListBinding
import ru.internetcloud.workorderapplication.databinding.ItemEmployeeListSelectedBinding
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee

class EmployeeListAdapter(var employees: List<Employee>) : RecyclerView.Adapter<EmployeeListViewHolder>() {
    var onEmployeeClickListener: ((employee: Employee) -> Unit)? = null
    var onEmployeeLongClickListener: ((employee: Employee) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeListViewHolder {
        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemEmployeeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemEmployeeListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return EmployeeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeListViewHolder, position: Int) {
        val employee = employees[position]

        val binding = holder.binding

        if (employee.isSelected) {
            val currentBinding = binding as ItemEmployeeListSelectedBinding
            currentBinding.nameTextView.text = employee.name
        } else {
            val currentBinding = binding as ItemEmployeeListBinding
            currentBinding.nameTextView.text = employee.name
        }

        binding.root.setOnClickListener {
            onEmployeeClickListener?.invoke(employee)
            notifyItemChanged(employees.indexOf(employee), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onEmployeeLongClickListener?.invoke(employee)
            true
        }
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun getItemViewType(position: Int): Int {
        val employee = employees[position]

        return if (employee.isSelected) {
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
