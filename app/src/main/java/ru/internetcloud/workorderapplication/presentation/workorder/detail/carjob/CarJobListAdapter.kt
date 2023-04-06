package ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemCarJobListBinding
import ru.internetcloud.workorderapplication.databinding.ItemCarJobListSelectedBinding
import ru.internetcloud.workorderapplication.domain.catalog.CarJob

class CarJobListAdapter(var carJobs: List<CarJob>) : RecyclerView.Adapter<CarJobListViewHolder>() {
    var onCarJobClickListener: ((carJob: CarJob) -> Unit)? = null
    var onCarJobLongClickListener: ((carJob: CarJob) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarJobListViewHolder {

        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemCarJobListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemCarJobListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return CarJobListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarJobListViewHolder, position: Int) {
        val carJob = carJobs[position]

        val binding = holder.binding

        if (carJob.isSelected) {
            val currentBinding = binding as ItemCarJobListSelectedBinding
            currentBinding.nameTextView.text = carJob.name
            currentBinding.folderTextView.text = carJob.folder
        } else {
            val currentBinding = binding as ItemCarJobListBinding
            currentBinding.nameTextView.text = carJob.name
            currentBinding.folderTextView.text = carJob.folder
        }

        binding.root.setOnClickListener {
            onCarJobClickListener?.invoke(carJob)
            notifyItemChanged(carJobs.indexOf(carJob), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onCarJobLongClickListener?.invoke(carJob)
            true
        }
    }

    override fun getItemCount(): Int {
        return carJobs.size
    }

    override fun getItemViewType(position: Int): Int {
        val carJob = carJobs[position]

        return if (carJob.isSelected) {
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
