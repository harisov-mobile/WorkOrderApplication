package ru.internetcloud.workorderapplication.presentation.workorder.detail.car

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemCarListBinding
import ru.internetcloud.workorderapplication.databinding.ItemCarListSelectedBinding
import ru.internetcloud.workorderapplication.domain.catalog.Car

class CarListAdapter(var cars: List<Car>) : RecyclerView.Adapter<CarListViewHolder>() {
    var onCarClickListener: ((car: Car) -> Unit)? = null
    var onCarLongClickListener: ((car: Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        var binding: ViewBinding? = null
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemCarListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemCarListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return CarListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        val car = cars[position]

        val binding = holder.binding

        if (car.isSelected) {
            val currentBinding = binding as ItemCarListSelectedBinding
            currentBinding.nameTextView.text = car.name
            currentBinding.manufacturerTextView.text = car.manufacturer
        } else {
            val currentBinding = binding as ItemCarListBinding
            currentBinding.nameTextView.text = car.name
            currentBinding.manufacturerTextView.text = car.manufacturer
        }

        binding.root.setOnClickListener {
            onCarClickListener?.invoke(car)
            notifyItemChanged(cars.indexOf(car), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onCarLongClickListener?.invoke(car)
            true
        }
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun getItemViewType(position: Int): Int {
        val car = cars[position]

        return if (car.isSelected) {
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
