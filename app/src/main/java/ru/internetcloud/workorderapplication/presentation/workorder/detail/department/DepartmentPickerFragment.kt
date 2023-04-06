package ru.internetcloud.workorderapplication.presentation.workorder.detail.department

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.di.ViewModelFactory

class DepartmentPickerFragment : DialogFragment() {

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var requestKey = ""
    private var argDepartmentName = ""

    private lateinit var viewModel: DepartmentListViewModel
    private lateinit var departmentListRecyclerView: RecyclerView
    private lateinit var departmentListAdapter: DepartmentListAdapter

    private lateinit var clearSearchTextImageButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // даггер:
        component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DepartmentListViewModel::class.java)

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
        // alertDialogBuilder.setTitle(R.string.department_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        clearSearchTextImageButton = container.findViewById(R.id.clear_search_text_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        titleTextView = container.findViewById(R.id.title_text_view)
        titleTextView.text = getString(R.string.department_picker_title)

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

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedDepartment = null
            } else {
                viewModel.selectedDepartment = departments[currentPosition]
                viewModel.selectedDepartment?.isSelected = true

                val scrollPosition = if (currentPosition > (departmentListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    departmentListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                departmentListRecyclerView.scrollToPosition(scrollPosition)
                departmentListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadDepartmentList() // самое главное!!! если это создание нового фрагмента
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

    private fun setupDepartmentListRecyclerView(view: View) {
        departmentListRecyclerView = view.findViewById(R.id.list_recycler_view)
        departmentListAdapter = DepartmentListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        departmentListRecyclerView.adapter = departmentListAdapter
    }

    private fun setupClickListeners() {
        departmentListAdapter.onDepartmentClickListener = { currentDepartment ->
            viewModel.selectedDepartment?.isSelected = false // предыдущий отмеченный - снимаем пометку
            viewModel.selectedDepartment = currentDepartment
            viewModel.selectedDepartment?.isSelected = true
        }

        departmentListAdapter.onDepartmentLongClickListener = { currentDepartment ->
            sendResultToFragment(currentDepartment)
            dismiss()
        }

        clearSearchTextImageButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.loadDepartmentList()
        }
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            viewModel.loadDepartmentList()
        } else {
            viewModel.searchDepartments(searchText)
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
}
