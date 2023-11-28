package ru.internetcloud.workorderapplication.presentation.workorder.detail.performers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.databinding.ItemPerformerDetailListBinding
import ru.internetcloud.workorderapplication.databinding.ItemPerformerDetailListSelectedBinding
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail

class PerformerDetailListViewHolder(
    private val binding: ViewBinding,
    private val performerDetailListListener: PerformerDetailListListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var performerDetail: PerformerDetail

    fun bind(performerDetail: PerformerDetail, viewType: Int) {
        this.performerDetail = performerDetail

        when (viewType) {
            PerformerDetailListAdapter.SELECTED_ITEM_TYPE -> {
                val currentBinding = binding as ItemPerformerDetailListSelectedBinding
                currentBinding.nameTextView.text = performerDetail.employee?.name
                currentBinding.lineNumberTextView.text = performerDetail.lineNumber.toString()
            }
            PerformerDetailListAdapter.UNSELECTED_ITEM_TYPE -> {
                val currentBinding = binding as ItemPerformerDetailListBinding
                currentBinding.nameTextView.text = performerDetail.employee?.name
                currentBinding.lineNumberTextView.text = performerDetail.lineNumber.toString()
            }
            else -> error("Unknown viewType = $viewType")
        }
    }

    override fun onClick(v: View?) {
        performerDetailListListener.onClickPerformerDetail(performerDetail)
    }
}
