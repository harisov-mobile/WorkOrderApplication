package ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.presentation.ViewModelFactory
import javax.inject.Inject

class WorkingHourPickerFragment : DialogFragment() {

    companion object {

        private const val WORKING_HOUR = "working_hour"
        private const val PARENT_REQUEST_KEY = "parent_request_working_hour_picker_key"
        private const val PARENT_WORKING_HOUR_ARG_NAME = "parent_working_hour_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(workingHour: WorkingHour?, parentRequestKey: String, parentArgDateName: String): WorkingHourPickerFragment {
            val args = Bundle().apply {
                putParcelable(WORKING_HOUR, workingHour)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_WORKING_HOUR_ARG_NAME, parentArgDateName)
            }
            return WorkingHourPickerFragment().apply {
                arguments = args
            }
        }
    }

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var requestKey = ""
    private var argWorkingHourName = ""

    private lateinit var viewModel: WorkingHourListViewModel
    private lateinit var workingHourListRecyclerView: RecyclerView
    private lateinit var workingHourListAdapter: WorkingHourListAdapter

    private lateinit var clearSearchTextButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // даггер:
        component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkingHourListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedWorkingHour ?: let {
                viewModel.selectedWorkingHour = arg.getParcelable(WORKING_HOUR)
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argWorkingHourName = arg.getString(PARENT_WORKING_HOUR_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in WorkingHourPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        // alertDialogBuilder.setTitle(R.string.working_fragment_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        clearSearchTextButton = container.findViewById(R.id.clear_search_text_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        titleTextView = container.findViewById(R.id.title_text_view)
        titleTextView.text = getString(R.string.working_fragment_picker_title)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedWorkingHour)
        }

        setupWorkingHourListRecyclerView(container)

        viewModel.workingHourListLiveData.observe(this, { workingHours ->
            workingHourListAdapter = WorkingHourListAdapter(workingHours)
            workingHourListRecyclerView.adapter = workingHourListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedWorkingHour, workingHourListAdapter.workingHours)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedWorkingHour = null
            } else {
                viewModel.selectedWorkingHour = workingHours[currentPosition]
                viewModel.selectedWorkingHour?.isSelected = true

                val scrollPosition = if (currentPosition > (workingHourListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    workingHourListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                workingHourListRecyclerView.scrollToPosition(scrollPosition)
                workingHourListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadWorkingHourList() // самое главное!!! если это создание нового фрагмента
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

    private fun setupWorkingHourListRecyclerView(view: View) {
        workingHourListRecyclerView = view.findViewById(R.id.list_recycler_view)
        workingHourListAdapter = WorkingHourListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        workingHourListRecyclerView.adapter = workingHourListAdapter
    }

    private fun setupClickListeners() {
        workingHourListAdapter.onWorkingHourClickListener = { currentWorkingHour ->
            viewModel.selectedWorkingHour?.isSelected = false // предыдущий отмеченный - снимаем пометку
            viewModel.selectedWorkingHour = currentWorkingHour
            viewModel.selectedWorkingHour?.isSelected = true
        }

        workingHourListAdapter.onWorkingHourLongClickListener = { currentWorkingHour ->
            sendResultToFragment(currentWorkingHour)
            dismiss()
        }

        clearSearchTextButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.loadWorkingHourList()
        }
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            viewModel.loadWorkingHourList()
        } else {
            viewModel.searchWorkingHours(searchText)
        }
    }

    private fun getPosition(searchedWorkingHour: WorkingHour?, workingHourList: List<WorkingHour>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedWorkingHour?.let {
            var isFound = false
            for (currentWorkingHour in workingHourList) {
                currentPosition++
                if (currentWorkingHour.id == searchedWorkingHour.id) {
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

    private fun sendResultToFragment(result: WorkingHour?) {
        val bundle = Bundle().apply {
            putParcelable(argWorkingHourName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
