package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrderBinding
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import java.lang.RuntimeException

class PartnerPickerFragment: DialogFragment() {

    companion object {

        private const val PARTNER = "partner"
        private const val PARENT_REQUEST_KEY = "parent_request_partner_id_picker_key"
        private const val PARENT_PARTNER_ARG_NAME = "parent_partner_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(partner: Partner?, parentRequestKey: String, parentArgDateName: String): PartnerPickerFragment {
            val args = Bundle().apply {
                putParcelable(PARTNER, partner)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_PARTNER_ARG_NAME, parentArgDateName)
            }
            return PartnerPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var partner: Partner? = null
    private var previousSelectedPartner: Partner? = null

    private var requestKey = ""
    private var argPartnerName = ""

    private lateinit var viewModel: PartnerListViewModel
    private lateinit var partnerListRecyclerView: RecyclerView
    private lateinit var partnerListAdapter: PartnerListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            partner = arg.getParcelable(PARTNER)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argPartnerName = arg.getString(PARENT_PARTNER_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in PartnerPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.partner_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_partner_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            sendResultToFragment(partner)
        }

        setupPartnerListRecyclerView(container)

        viewModel = ViewModelProvider(this).get(PartnerListViewModel::class.java)
        viewModel.partnerListLiveData.observe(this, { partners ->
            partnerListAdapter = PartnerListAdapter(partners)
            partnerListRecyclerView.adapter = partnerListAdapter
            setupClickListeners()

            val currentPosition = getCurrentPosition(partner)

            val scrollPosition = if (currentPosition > (partnerListAdapter.getItemCount() - DIFFERENCE_POS)) {
                partnerListAdapter.getItemCount() - 1
            } else {
                currentPosition
            }

            partnerListRecyclerView.scrollToPosition(scrollPosition);

            if (currentPosition != NOT_FOUND_POSITION) {
                partners[currentPosition].isSelected = true
                // partnerListAdapter.notifyDataSetChanged()
                partnerListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        viewModel.loadPartnerList() // самое главное!!!

        return alertDialogBuilder.create()
    }

    private fun setupPartnerListRecyclerView(view: View) {
        partnerListRecyclerView = view.findViewById(R.id.partner_list_recycler_view)
        partnerListAdapter = PartnerListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        partnerListRecyclerView.adapter = partnerListAdapter
    }

    private fun setupClickListeners() {
        partnerListAdapter.onPartnerClickListener = { currentPartner ->
            partner = currentPartner
            previousSelectedPartner?.isSelected = false
            partner?.isSelected = true
            previousSelectedPartner = partner
            Log.i("rustam", " onPartnerSelected = " + partner?.name)
            Log.i("rustam", " partnerId = " + partner?.id)
        }

        partnerListAdapter.onPartnerLongClickListener = { currentPartner ->
            sendResultToFragment(currentPartner)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadPartnerList()
            } else {
                viewModel.searchPartners(searchText)
            }
        }
    }

    private fun getCurrentPosition(searchedPartner: Partner?): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedPartner?.let {
            var isFound = false
            for (currentPartner in partnerListAdapter.partners) {
                currentPosition++
                if (currentPartner.id == searchedPartner.id) {
                    isFound = true
                    previousSelectedPartner = currentPartner
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }

    private fun sendResultToFragment(result: Partner?) {
        val bundle = Bundle().apply {
            putParcelable(argPartnerName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
