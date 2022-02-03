package ru.internetcloud.workorderapplication.presentation.workorder.detail.department

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
import ru.internetcloud.workorderapplication.domain.catalog.Department

class DepartmentPickerFragment : DialogFragment() {

    companion object {

        private const val DEPARTMENT = "department"
        private const val PARENT_REQUEST_KEY = "parent_request_department_picker_key"
        private const val PARENT_DEPARTMENT_ARG_NAME = "parent_department_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(department: Department?, parentRequestKey: String, parentArgName: String): DepartmentPickerFragment {
            val args = Bundle().apply {
                putParcelable(DEPARTMENT, department)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_DEPARTMENT_ARG_NAME, parentArgName)
            }
            return DepartmentPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var requestKey = ""
    private var argDepartmentName = ""

    private lateinit var viewModel: DepartmentListViewModel
    private lateinit var departmentListRecyclerView: RecyclerView
    private lateinit var departmentListAdapter: DepartmentListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this).get(DepartmentListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedDepartment ?: let {
                viewModel.selectedDepartment = arg.getParcelable(DEPARTMENT)
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argDepartmentName = arg.getString(PARENT_DEPARTMENT_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in DepartmentPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.department_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedDepartment)
        }

        setupDepartmentListRecyclerView(container)

        viewModel.departmentListLiveData.observe(this, { departments ->
            departmentListAdapter = DepartmentListAdapter(departments)
            departmentListRecyclerView.adapter = departmentListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedDepartment, departmentListAdapter.departments)

            if (currentPosition != NOT_FOUND_POSITION) {
                departments[currentPosition].isSelected = true

                val scrollPosition = if (currentPosition > (departmentListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    departmentListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                departmentListRecyclerView.scrollToPosition(scrollPosition)
                departmentListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        viewModel.loadDepartmentList() // самое главное!!!

        return alertDialogBuilder.create()
    }

    private fun setupDepartmentListRecyclerView(view: View) {
        departmentListRecyclerView = view.findViewById(R.id.list_recycler_view)
        departmentListAdapter = DepartmentListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        departmentListRecyclerView.adapter = departmentListAdapter
    }

    private fun setupClickListeners() {
        departmentListAdapter.onDepartmentClickListener = { currentDepartment ->
            departmentListAdapter.departments.forEach {
                it.isSelected = false // снимем пометку у всех
            }
            viewModel.selectedDepartment = currentDepartment
            viewModel.selectedDepartment?.isSelected = true
        }

        departmentListAdapter.onDepartmentLongClickListener = { currentDepartment ->
            sendResultToFragment(currentDepartment)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadDepartmentList()
            } else {
                viewModel.searchDepartments(searchText)
            }
        }
    }

    private fun getPosition(searchedDepartment: Department?, departmentList: List<Department>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedDepartment?.let {
            var isFound = false
            for (currentDepartment in departmentList) {
                currentPosition++
                if (currentDepartment.id == searchedDepartment.id) {
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

    private fun sendResultToFragment(result: Department?) {
        val bundle = Bundle().apply {
            putParcelable(argDepartmentName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
