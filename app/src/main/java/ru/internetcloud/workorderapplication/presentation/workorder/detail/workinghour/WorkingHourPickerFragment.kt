package ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour

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
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

class WorkingHourPickerFragment : DialogFragment() {

    companion object {

        private const val WORKING_FRAGMENT = "working_fragment"
        private const val PARENT_REQUEST_KEY = "parent_request_working_fragment_picker_key"
        private const val PARENT_WORKING_FRAGMENT_ARG_NAME = "parent_working_fragment_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(workingHour: WorkingHour?, parentRequestKey: String, parentArgDateName: String): WorkingHourPickerFragment {
            val args = Bundle().apply {
                putParcelable(WORKING_FRAGMENT, workingHour)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_WORKING_FRAGMENT_ARG_NAME, parentArgDateName)
            }
            return WorkingHourPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var workingHour: WorkingHour? = null
    private var previousSelectedWorkingHour: WorkingHour? = null

    private var requestKey = ""
    private var argWorkingHourName = ""

    private lateinit var viewModel: WorkingHourListViewModel
    private lateinit var workingHourListRecyclerView: RecyclerView
    private lateinit var workingHourListAdapter: WorkingHourListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            workingHour = arg.getParcelable(WORKING_FRAGMENT)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argWorkingHourName = arg.getString(PARENT_WORKING_FRAGMENT_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in WorkingHourPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.working_fragment_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            sendResultToFragment(workingHour)
        }

        setupWorkingHourListRecyclerView(container)

        viewModel = ViewModelProvider(this).get(WorkingHourListViewModel::class.java)
        viewModel.workingHourListLiveData.observe(this, { partners ->
            workingHourListAdapter = WorkingHourListAdapter(partners)
            workingHourListRecyclerView.adapter = workingHourListAdapter
            setupClickListeners()

            val currentPosition = getCurrentPosition(workingHour)

            val scrollPosition = if (currentPosition > (workingHourListAdapter.getItemCount() - DIFFERENCE_POS)) {
                workingHourListAdapter.getItemCount() - 1
            } else {
                currentPosition
            }

            workingHourListRecyclerView.scrollToPosition(scrollPosition)

            if (currentPosition != NOT_FOUND_POSITION) {
                partners[currentPosition].isSelected = true
                workingHourListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        viewModel.loadWorkingHourList() // самое главное!!!

        return alertDialogBuilder.create()
    }

    private fun setupWorkingHourListRecyclerView(view: View) {
        workingHourListRecyclerView = view.findViewById(R.id.list_recycler_view)
        workingHourListAdapter = WorkingHourListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        workingHourListRecyclerView.adapter = workingHourListAdapter
    }

    private fun setupClickListeners() {
        workingHourListAdapter.onWorkingHourClickListener = { currentWorkingHour ->
            workingHour = currentWorkingHour
            previousSelectedWorkingHour?.isSelected = false
            workingHour?.isSelected = true
            previousSelectedWorkingHour = workingHour
        }

        workingHourListAdapter.onWorkingHourLongClickListener = { currentWorkingHour ->
            sendResultToFragment(currentWorkingHour)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadWorkingHourList()
            } else {
                viewModel.searchWorkingHours(searchText)
            }
        }
    }

    private fun getCurrentPosition(searchedWorkingHour: WorkingHour?): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedWorkingHour?.let {
            var isFound = false
            for (currentWorkingHour in workingHourListAdapter.workingHours) {
                currentPosition++
                if (currentWorkingHour.id == searchedWorkingHour.id) {
                    isFound = true
                    previousSelectedWorkingHour = currentWorkingHour
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }

    private fun sendResultToFragment(result: WorkingHour?) {
        val bundle = Bundle().apply {
            putParcelable(argWorkingHourName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
