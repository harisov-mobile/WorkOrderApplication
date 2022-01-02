package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.ItemPartnerListBinding
import ru.internetcloud.workorderapplication.databinding.ItemPartnerListSelectedBinding
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import java.lang.RuntimeException

class PartnerListAdapter(var partners: List<Partner>) : RecyclerView.Adapter<PartnerListViewHolder>()
{
    var onPartnerClickListener: ((partner: Partner) -> Unit)? = null
    var onPartnerLongClickListener: ((partner: Partner) -> Unit)? = null

    companion object {
        private const val SELECTED_ITEM_TYPE = 0
        private const val UNSELECTED_ITEM_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerListViewHolder {

        var binding: ViewBinding? = null
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
}
