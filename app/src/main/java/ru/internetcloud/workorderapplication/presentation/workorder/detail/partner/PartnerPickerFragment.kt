package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import java.lang.RuntimeException

class PartnerPickerFragment: DialogFragment() {

    companion object {

        private const val PARTNER_ID = "partner_id"
        private const val PARENT_REQUEST_KEY = "parent_request_partner_id_picker_key"
        private const val PARENT_ARG_PARTNER_ID_NAME = "parent_arg_partner_id_name"

        fun newInstance(partnerId: String, parentRequestKey: String, parentArgDateName: String): PartnerPickerFragment {
            val args = Bundle().apply {
                putString(PARTNER_ID, partnerId)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_ARG_PARTNER_ID_NAME, parentArgDateName)
            }
            return PartnerPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var partnerId = ""
    private lateinit var viewModel: PartnerListViewModel
    private lateinit var partnerListRecyclerView: RecyclerView
    private lateinit var partnerListAdapter: PartnerListAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var requestKey = ""
        var argPartnerIdName = ""

        arguments?.let { arg ->
            partnerId = arg.getString(PARTNER_ID, "")
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argPartnerIdName = arg.getString(PARENT_ARG_PARTNER_ID_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in PartnerPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.partner_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_partner_picker, null, false)
        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
            val bundle = Bundle().apply {
                putString(argPartnerIdName, null)
            }
            setFragmentResult(requestKey, bundle)
        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            val bundle = Bundle().apply {
                putString(argPartnerIdName, partnerId)
            }
            setFragmentResult(requestKey, bundle)
        }

        setupPartnerListRecyclerView(container)

        viewModel = ViewModelProvider(this).get(PartnerListViewModel::class.java)
        viewModel.partnerListLiveData.observe(this, { partners ->
            partnerListAdapter = PartnerListAdapter(partners)
            partnerListRecyclerView.adapter = partnerListAdapter
            setupClickListener()
            partnerListRecyclerView.scrollToPosition(getCurrentPositionToId(partnerId));
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

    private fun setupClickListener() {
        partnerListAdapter.onPartnerClickListener = { partner ->
            partnerId = partner.id
            Log.i("rustam", " onPartnerSelected = " + partner.name)
            Log.i("rustam", " partnerId = " + partnerId)
        }
    }

    private fun getCurrentPositionToId(id: String): Int {
        var currentPosition = -1
        if (!id.isEmpty()) {
            var isFound = false
            for (currentPartner in partnerListAdapter.partners) {
                currentPosition++
                if (currentPartner.id == id) {
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                currentPosition = -1
            }
        }
        return currentPosition
    }
}
