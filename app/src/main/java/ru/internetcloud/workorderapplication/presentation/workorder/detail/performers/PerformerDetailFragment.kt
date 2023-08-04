package ru.internetcloud.workorderapplication.presentation.workorder.detail.performers

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.common.ValidateInputResult
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.employee.EmployeePickerFragment

class PerformerDetailFragment : DialogFragment(), FragmentResultListener {

    private var requestKey = ""
    private var argPerformerDetailName = ""

    private lateinit var viewModel: PerformerDetailViewModel
    private lateinit var employeeSelectButton: Button
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var employeeTextView: TextView
    private lateinit var lineNumberTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this).get(PerformerDetailViewModel::class.java)

        arguments?.let { arg ->
            savedInstanceState ?: let {
                val performerDetail: PerformerDetail? = arg.getParcelable(PERFORMER_DETAIL)
                performerDetail ?: let {
                    throw RuntimeException("performerDetail is Null in PerformerDetailFragment")
                }
                viewModel.performerDetail = PerformerDetail(
                    id = performerDetail.id,
                    lineNumber = performerDetail.lineNumber,
                    employee = performerDetail.employee,
                    isSelected = performerDetail.isSelected
                )
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argPerformerDetailName = arg.getString(PARENT_PERFORMER_DETAIL_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in PerformerDetailFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.performer_detail_title)

        val container = layoutInflater.inflate(R.layout.fragment_performer_detail, null, false)
        employeeSelectButton = container.findViewById(R.id.employee_select_button)
        okButton = container.findViewById(R.id.ok_button)
        cancelButton = container.findViewById(R.id.cancel_button)
        employeeTextView = container.findViewById(R.id.employee_text_view)
        lineNumberTextView = container.findViewById(R.id.line_number_text_view)

        alertDialogBuilder.setView(container)

        setupViews()

        setupClickListeners()

        // viewLifecycleOwner - здесь не может быть получен, вместо него this использую
        childFragmentManager.setFragmentResultListener(REQUEST_EMPLOYEE_PICKER_KEY, this, this)

        return alertDialogBuilder.create()
    }

    private fun setupViews() {
        viewModel.performerDetail?.let { currentPerformerDetail ->
            lineNumberTextView.text = currentPerformerDetail.lineNumber.toString()
            employeeTextView.text = currentPerformerDetail.employee?.name ?: ""
        }
    }

    private fun setupClickListeners() {
        // выбор "Исполнителя"
        employeeSelectButton.setOnClickListener {
            viewModel.performerDetail?.let { currentPerformerDetail ->

                EmployeePickerFragment
                    .newInstance(currentPerformerDetail.employee, REQUEST_EMPLOYEE_PICKER_KEY, ARG_EMPLOYEE)
                    .show(childFragmentManager, REQUEST_EMPLOYEE_PICKER_KEY)
            }
        }

        // Ok
        okButton.setOnClickListener {
            val validateInputResult = validateInput()
            if (validateInputResult.isValid) {
                sendResultToFragment(viewModel.performerDetail)
                dialog?.dismiss()
            } else {
                MessageDialogFragment.newInstance(validateInputResult.errorMessage)
                    .show(childFragmentManager, null)
            }
        }

        // Cancel
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun sendResultToFragment(result: PerformerDetail?) {
        val bundle = Bundle().apply {
            putParcelable(argPerformerDetailName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_EMPLOYEE_PICKER_KEY -> {
                val employee: Employee? = result.getParcelable(ARG_EMPLOYEE)
                viewModel.performerDetail?.let { currentPerformerDetail ->
                    currentPerformerDetail.employee = employee
                    employeeTextView.text = employee?.name ?: ""
                }
            }
        }
    }

    private fun validateInput(): ValidateInputResult {
        var result = ValidateInputResult(true, "")
        viewModel.performerDetail?.let { currentPerformerDetail ->
            currentPerformerDetail.employee ?: let {
                result.isValid = false
                result.errorMessage = result.errorMessage + getString(R.string.error_empty_employee) + "\n"
            }
        }
        return result
    }

    companion object {

        private const val PERFORMER_DETAIL = "performer_detail"
        private const val PARENT_REQUEST_KEY = "parent_request_performer_detail_picker_key"
        private const val PARENT_PERFORMER_DETAIL_ARG_NAME = "parent_performer_detail_arg_name"

        private val REQUEST_EMPLOYEE_PICKER_KEY = "request_employee_picker_key"
        private val ARG_EMPLOYEE = "employee_picker"

        fun newInstance(
            performerDetail: PerformerDetail?,
            parentRequestKey: String,
            parentArgName: String
        ): PerformerDetailFragment {
            val args = Bundle().apply {
                putParcelable(PERFORMER_DETAIL, performerDetail)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_PERFORMER_DETAIL_ARG_NAME, parentArgName)
            }
            return PerformerDetailFragment().apply {
                arguments = args
            }
        }
    }
}
