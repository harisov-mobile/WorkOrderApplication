package ru.internetcloud.workorderapplication.workorderdetail.presentation.partner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemPartnerListBinding
import ru.internetcloud.workorderapplication.workorderdetail.databinding.ItemPartnerListSelectedBinding

class PartnerListAdapter(var partners: List<Partner>) : RecyclerView.Adapter<PartnerListViewHolder>() {
    var onPartnerClickListener: ((partner: Partner) -> Unit)? = null
    var onPartnerLongClickListener: ((partner: Partner) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerListViewHolder {
        val binding: ViewBinding?
        if (viewType == UNSELECTED_ITEM_TYPE) {
            binding = ItemPartnerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (viewType == SELECTED_ITEM_TYPE) {
            binding = ItemPartnerListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            throw RuntimeException("Unknown viewType : $viewType")
        }
        return PartnerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerListViewHolder, position: Int) {
        val partner = partners[position]

        val binding = holder.binding

        if (partner.isSelected) {
            val currentBinding = binding as ItemPartnerListSelectedBinding
            currentBinding.nameTextView.text = partner.name
            currentBinding.innTextView.text = partner.inn
        } else {
            val currentBinding = binding as ItemPartnerListBinding
            currentBinding.nameTextView.text = partner.name
            currentBinding.innTextView.text = partner.inn
        }

        binding.root.setOnClickListener {
            onPartnerClickListener?.invoke(partner)
            notifyItemChanged(partners.indexOf(partner), Unit) // перерисовка без анимации
        }
        binding.root.setOnLongClickListener {
            onPartnerLongClickListener?.invoke(partner)
            true
        }
    }

    override fun getItemCount(): Int {
        return partners.size
    }

    override fun getItemViewType(position: Int): Int {
        val partner = partners[position]

        return if (partner.isSelected) {
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
