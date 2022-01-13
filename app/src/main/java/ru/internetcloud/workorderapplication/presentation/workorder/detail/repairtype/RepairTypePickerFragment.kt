package ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import java.lang.RuntimeException

class RepairTypePickerFragment : DialogFragment() {

    companion object {

        private const val REPAIR_TYPE = "repair_type"
        private const val PARENT_REQUEST_KEY = "parent_request_repair_type_picker_key"
        private const val PARENT_REPAIR_TYPE_ARG_NAME = "parent_repair_type_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(repairType: RepairType?, parentRequestKey: String, parentArgDateName: String): RepairTypePickerFragment {
            val args = Bundle().apply {
                putParcelable(REPAIR_TYPE, repairType)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_REPAIR_TYPE_ARG_NAME, parentArgDateName)
            }
            return RepairTypePickerFragment().apply {
                arguments = args
            }
        }
    }

    private var repairType: RepairType? = null
    private var previousSelectedRepairType: RepairType? = null

    private var requestKey = ""
    private var argRepairTypeName = ""

    private lateinit var viewModel: RepairTypeListViewModel
    private lateinit var repairTypeListRecyclerView: RecyclerView
    private lateinit var repairTypeListAdapter: RepairTypeListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            repairType = arg.getParcelable(REPAIR_TYPE)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argRepairTypeName = arg.getString(PARENT_REPAIR_TYPE_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in RepairTypePickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.repair_type_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            sendResultToFragment(repairType)
        }

        setupRepairTypeListRecyclerView(container)

        viewModel = ViewModelProvider(this).get(RepairTypeListViewModel::class.java)
        viewModel.repairTypeListLiveData.observe(this, { repairTypes ->
            repairTypeListAdapter = RepairTypeListAdapter(repairTypes)
            repairTypeListRecyclerView.adapter = repairTypeListAdapter
            setupClickListeners()

            val currentPosition = getCurrentPosition(repairType)

            val scrollPosition = if (currentPosition > (repairTypeListAdapter.getItemCount() - RepairTypePickerFragment.DIFFERENCE_POS)) {
                repairTypeListAdapter.getItemCount() - 1
            } else {
                currentPosition
            }

            repairTypeListRecyclerView.scrollToPosition(scrollPosition)

            if (currentPosition != NOT_FOUND_POSITION) {
                repairTypes[currentPosition].isSelected = true
                repairTypeListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        viewModel.loadRepairTypeList() // самое главное!!!

        return alertDialogBuilder.create()
    }

    private fun setupRepairTypeListRecyclerView(view: View) {
        repairTypeListRecyclerView = view.findViewById(R.id.list_recycler_view)
        repairTypeListAdapter = RepairTypeListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        repairTypeListRecyclerView.adapter = repairTypeListAdapter
    }

    private fun setupClickListeners() {
        repairTypeListAdapter.onRepairTypeClickListener = { currentRepairType ->
            repairType = currentRepairType
            previousSelectedRepairType?.isSelected = false
            repairType?.isSelected = true
            previousSelectedRepairType = repairType
        }

        repairTypeListAdapter.onRepairTypeLongClickListener = { currentRepairType ->
            sendResultToFragment(currentRepairType)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadRepairTypeList()
            } else {
                viewModel.searchRepairTypes(searchText)
            }
        }
    }

    private fun getCurrentPosition(searchedRepairType: RepairType?): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedRepairType?.let {
            var isFound = false
            for (currentRepairType in repairTypeListAdapter.repairTypes) {
                currentPosition++
                if (currentRepairType.id == searchedRepairType.id) {
                    isFound = true
                    previousSelectedRepairType = currentRepairType
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }

    private fun sendResultToFragment(result: RepairType?) {
        val bundle = Bundle().apply {
            putParcelable(argRepairTypeName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
