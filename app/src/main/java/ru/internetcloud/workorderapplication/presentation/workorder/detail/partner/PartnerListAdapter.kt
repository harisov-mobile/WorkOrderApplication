package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.databinding.ItemPartnerListBinding
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class PartnerListAdapter(var partners: List<Partner>) : RecyclerView.Adapter<PartnerListViewHolder>()
//ListAdapter<Partner, PartnerListViewHolder>() {
{
    var onPartnerClickListener: ((partner: Partner) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerListViewHolder {
        val binding = ItemPartnerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerListViewHolder, position: Int) {
        val partner = partners[position]
        val binding = holder.binding

        binding.nameTextView.text = partner.name
        binding.innTextView.text = partner.inn

        binding.root.setOnClickListener {
            onPartnerClickListener?.invoke(partner)
            Log.i("rustam", " нажат элемент " + partner.name)
        }
    }

    override fun getItemCount(): Int {
        return partners.size
    }
}
