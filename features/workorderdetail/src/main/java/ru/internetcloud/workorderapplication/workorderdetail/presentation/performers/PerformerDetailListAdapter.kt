package ru.internetcloud.workorderapplication.workorderdetail.presentation.performers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.internetcloud.workorderapplication.common.domain.model.document.PerformerDetail
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemPerformerDetailListBinding
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemPerformerDetailListSelectedBinding

class PerformerDetailListAdapter(
    private val performerDetailListListener: PerformerDetailListListener
) : ListAdapter<PerformerDetail, PerformerDetailListViewHolder>(PerformerDetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerDetailListViewHolder {
        lateinit var performerDetailListViewHolder: PerformerDetailListViewHolder

        val binding = when (viewType) {
            UNSELECTED_ITEM_TYPE -> {
                ItemPerformerDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            SELECTED_ITEM_TYPE -> {
                ItemPerformerDetailListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            else -> error("Unknown viewType : $viewType")
        }

        performerDetailListViewHolder = PerformerDetailListViewHolder(binding, performerDetailListListener)

        // назначаем обработчик кликов для всего элемента списка:
        binding.root.setOnClickListener(performerDetailListViewHolder)

        return performerDetailListViewHolder
    }

    override fun onBindViewHolder(holder: PerformerDetailListViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        val performerDetail = getItem(position)

        return if (performerDetail.isSelected) {
            SELECTED_ITEM_TYPE
        } else {
            UNSELECTED_ITEM_TYPE
        }
    }

    companion object {
        const val SELECTED_ITEM_TYPE = 0
        const val UNSELECTED_ITEM_TYPE = 1
    }
}
