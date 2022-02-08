package ru.internetcloud.workorderapplication.presentation.workorder.detail.employee

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
import ru.internetcloud.workorderapplication.domain.catalog.Employee

class EmployeePickerFragment : DialogFragment() {

    companion object {

        private const val EMPLOYEE = "employee"
        private const val PARENT_REQUEST_KEY = "parent_request_employee_picker_key"
        private const val PARENT_EMPLOYEE_ARG_NAME = "parent_employee_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(employee: Employee?, parentRequestKey: String, parentArgDateName: String): EmployeePickerFragment {
            val args = Bundle().apply {
                putParcelable(EMPLOYEE, employee)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_EMPLOYEE_ARG_NAME, parentArgDateName)
            }
            return EmployeePickerFragment().apply {
                arguments = args
            }
        }
    }

    private var requestKey = ""
    private var argEmployeeName = ""

    private lateinit var viewModel: EmployeeListViewModel
    private lateinit var employeeListRecyclerView: RecyclerView
    private lateinit var employeeListAdapter: EmployeeListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this).get(EmployeeListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedEmployee ?: let {
                viewModel.selectedEmployee = arg.getParcelable(EMPLOYEE)
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argEmployeeName = arg.getString(PARENT_EMPLOYEE_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in EmployeePickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.employee_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedEmployee)
        }

        setupEmployeeListRecyclerView(container)

        viewModel.employeeListLiveData.observe(this, { employees ->
            employeeListAdapter = EmployeeListAdapter(employees)
            employeeListRecyclerView.adapter = employeeListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedEmployee, employeeListAdapter.employees)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedEmployee = null
            } else {
                viewModel.selectedEmployee = employees[currentPosition]
                viewModel.selectedEmployee?.isSelected = true

                val scrollPosition = if (currentPosition > (employeeListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    employeeListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                employeeListRecyclerView.scrollToPosition(scrollPosition)
                employeeListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadEmployeeList() // самое главное!!! если это создание нового фрагмента
        }

        return alertDialogBuilder.create()
    }

    private fun setupEmployeeListRecyclerView(view: View) {
        employeeListRecyclerView = view.findViewById(R.id.list_recycler_view)
        employeeListAdapter = EmployeeListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        employeeListRecyclerView.adapter = employeeListAdapter
    }

    private fun setupClickListeners() {
        employeeListAdapter.onEmployeeClickListener = { currentEmployee ->
            viewModel.selectedEmployee?.isSelected = false // предыдущий отмеченный - снимаем пометку
            viewModel.selectedEmployee = currentEmployee
            viewModel.selectedEmployee?.isSelected = true
        }

        employeeListAdapter.onEmployeeLongClickListener = { currentEmployee ->
            sendResultToFragment(currentEmployee)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadEmployeeList()
            } else {
                viewModel.searchEmployees(searchText)
            }
        }
    }

    private fun getPosition(searchedEmployee: Employee?, employeeList: List<Employee>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedEmployee?.let {
            var isFound = false
            for (currentEmployee in employeeList) {
                currentPosition++
                if (currentEmployee.id == searchedEmployee.id) {
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

    private fun sendResultToFragment(result: Employee?) {
        val bundle = Bundle().apply {
            putParcelable(argEmployeeName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
