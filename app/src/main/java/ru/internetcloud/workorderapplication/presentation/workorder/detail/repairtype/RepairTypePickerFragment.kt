package ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType

@AndroidEntryPoint
class RepairTypePickerFragment : DialogFragment() {

    private var requestKey = ""
    private var argRepairTypeName = ""

    private val viewModel by viewModels<RepairTypeListViewModel>()

    private lateinit var repairTypeListRecyclerView: RecyclerView
    private lateinit var repairTypeListAdapter: RepairTypeListAdapter

    private lateinit var clearSearchTextImageButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let { arg ->
            viewModel.selectedRepairType ?: let {
                viewModel.selectedRepairType = arg.getParcelable(REPAIR_TYPE)
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argRepairTypeName = arg.getString(PARENT_REPAIR_TYPE_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in RepairTypePickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        // alertDialogBuilder.setTitle(R.string.repair_type_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        clearSearchTextImageButton = container.findViewById(R.id.clear_search_text_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        titleTextView = container.findViewById(R.id.title_text_view)
        titleTextView.text = getString(R.string.repair_type_picker_title)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedRepairType)
        }

        setupRepairTypeListRecyclerView(container)

        viewModel.repairTypeListLiveData.observe(this, { repairTypes ->
            repairTypeListAdapter = RepairTypeListAdapter(repairTypes)
            repairTypeListRecyclerView.adapter = repairTypeListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedRepairType, repairTypeListAdapter.repairTypes)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedRepairType = null
            } else {
                viewModel.selectedRepairType = repairTypes[currentPosition]
                viewModel.selectedRepairType?.isSelected = true

                val scrollPosition = if (currentPosition > (repairTypeListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    repairTypeListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                repairTypeListRecyclerView.scrollToPosition(scrollPosition)
                repairTypeListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadRepairTypeList() // самое главное!!! если это создание нового фрагмента
        }

        return alertDialogBuilder.create()
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // viewModel.resetErrorInputNumber()
            }

            override fun afterTextChanged(p0: Editable?) {
                search(p0?.toString() ?: "")
            }
        })
    }

    private fun setupRepairTypeListRecyclerView(view: View) {
        repairTypeListRecyclerView = view.findViewById(R.id.list_recycler_view)
        repairTypeListAdapter = RepairTypeListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        repairTypeListRecyclerView.adapter = repairTypeListAdapter
    }

    private fun setupClickListeners() {
        repairTypeListAdapter.onRepairTypeClickListener = { currentRepairType ->
            viewModel.selectedRepairType?.isSelected = false // предыдущий отмеченный - снимаем пометку
            viewModel.selectedRepairType = currentRepairType
            viewModel.selectedRepairType?.isSelected = true
        }

        repairTypeListAdapter.onRepairTypeLongClickListener = { currentRepairType ->
            sendResultToFragment(currentRepairType)
            dismiss()
        }

        clearSearchTextImageButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.loadRepairTypeList()
        }
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            viewModel.loadRepairTypeList()
        } else {
            viewModel.searchRepairTypes(searchText)
        }
    }

    private fun getPosition(searchedRepairType: RepairType?, repairTypeList: List<RepairType>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedRepairType?.let {
            var isFound = false
            for (currentRepairType in repairTypeList) {
                currentPosition++
                if (currentRepairType.id == searchedRepairType.id) {
                    isFound = true
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
}
