package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.common.ValidateInputResult
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob.CarJobPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour.WorkingHourPickerFragment
import java.math.BigDecimal

class JobDetailFragment : DialogFragment(), FragmentResultListener {

    companion object {

        private const val JOB_DETAIL = "job_detail"
        private const val PARENT_REQUEST_KEY = "parent_request_job_detail_picker_key"
        private const val PARENT_JOB_DETAIL_ARG_NAME = "parent_job_detail_arg_name"

        private val REQUEST_CAR_JOB_PICKER_KEY = "request_car_job_picker_key"
        private val ARG_CAR_JOB = "car_job_picker"

        private val REQUEST_WORKING_HOUR_PICKER_KEY = "request_working_hour_picker_key"
        private val ARG_WORKING_HOUR = "working_hour_picker"

        fun newInstance(jobDetail: JobDetail?, parentRequestKey: String, parentArgName: String): JobDetailFragment {
            val args = Bundle().apply {
                putParcelable(JOB_DETAIL, jobDetail)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_JOB_DETAIL_ARG_NAME, parentArgName)
            }
            return JobDetailFragment().apply {
                arguments = args
            }
        }
    }

    private var jobDetail: JobDetail? = null
    private var requestKey = ""
    private var argJobDetailName = ""

    private lateinit var viewModel: JobDetailViewModel
    private lateinit var carJobSelectButton: Button
    private lateinit var workingHourSelectButton: Button
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var carJobTextView: TextView
    private lateinit var workingHourTextView: TextView
    private lateinit var lineNumberTextView: TextView
    private lateinit var sumTextView: TextView
    private lateinit var quantityEditText: EditText
    private lateinit var timeNormEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            jobDetail = arg.getParcelable(JOB_DETAIL)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argJobDetailName = arg.getString(PARENT_JOB_DETAIL_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in JobDetailFragment")
        }

        jobDetail ?: let {
            throw RuntimeException("jobDetail is Null in JobDetailFragment")
        }

        viewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
        viewModel.jobDetail = jobDetail

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.job_detail_title)

        val container = layoutInflater.inflate(R.layout.fragment_job_detail, null, false)
        carJobSelectButton = container.findViewById(R.id.car_job_select_button)
        workingHourSelectButton = container.findViewById(R.id.working_hour_select_button)
        okButton = container.findViewById(R.id.ok_button)
        cancelButton = container.findViewById(R.id.cancel_button)
        carJobTextView = container.findViewById(R.id.car_job_text_view)
        workingHourTextView = container.findViewById(R.id.working_hour_text_view)
        lineNumberTextView = container.findViewById(R.id.line_number_text_view)
        quantityEditText = container.findViewById(R.id.quantity_edit_text)
        timeNormEditText = container.findViewById(R.id.time_norm_edit_text)
        sumTextView = container.findViewById(R.id.sum_text_view)

        alertDialogBuilder.setView(container)

        setupViews()

        setupClickListeners()

        // viewLifecycleOwner - здесь не может быть получен, вместо него this использую
        childFragmentManager.setFragmentResultListener(REQUEST_CAR_JOB_PICKER_KEY, this, this)
        childFragmentManager.setFragmentResultListener(REQUEST_WORKING_HOUR_PICKER_KEY, this, this)

        return alertDialogBuilder.create()
    }

    override fun onStart() {
        super.onStart()

        quantityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateSum()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        timeNormEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateSum()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setupViews() {
        viewModel.jobDetail?.let { jobdet ->
            lineNumberTextView.text = jobdet.lineNumber.toString()
            carJobTextView.text = jobdet.carJob?.name ?: ""
            workingHourTextView.text = jobdet.workingHour?.name ?: ""
            quantityEditText.setText(jobdet.quantity.toString())
            timeNormEditText.setText(jobdet.timeNorm.toString())
            sumTextView.text = jobdet.sum.toString()
        }
    }

    private fun setupClickListeners() {
        // выбор "Работы"
        carJobSelectButton.setOnClickListener {
            viewModel.jobDetail?.let { jobdet ->

                CarJobPickerFragment
                    .newInstance(jobdet.carJob, REQUEST_CAR_JOB_PICKER_KEY, ARG_CAR_JOB)
                    .show(childFragmentManager, REQUEST_CAR_JOB_PICKER_KEY)
            }
        }

        // выбор Нормо-часа
        workingHourSelectButton.setOnClickListener {
            viewModel.jobDetail?.let { jobdet ->

                WorkingHourPickerFragment
                    .newInstance(jobdet.workingHour, REQUEST_WORKING_HOUR_PICKER_KEY, ARG_WORKING_HOUR)
                    .show(childFragmentManager, REQUEST_WORKING_HOUR_PICKER_KEY)
            }
        }

        // Ok
        okButton.setOnClickListener {
            val validateInputResult = validateInput()
            if (validateInputResult.isValid) {
                sendResultToFragment(jobDetail)
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

    private fun calculateSum() {

        var sumString = "0"

        viewModel.jobDetail?.let { jobdet ->
            val quaintityString = quantityEditText.text.toString()
            if (quaintityString.isEmpty()) {
                jobdet.quantity = BigDecimal.ZERO
            } else {
                jobdet.quantity = quaintityString.toBigDecimal()
            }

            val timeNormString = timeNormEditText.text.toString()
            if (timeNormString.isEmpty()) {
                jobdet.timeNorm = BigDecimal.ZERO
            } else {
                jobdet.timeNorm = timeNormString.toBigDecimal()
            }

            jobdet.sum = BigDecimal.ZERO
            jobdet.workingHour?.let { wh ->
                jobdet.sum = wh.price * jobdet.quantity * jobdet.timeNorm
                sumString = jobdet.sum.toString()
            }
        }
        sumTextView.text = sumString
    }

    private fun sendResultToFragment(result: JobDetail?) {
        val bundle = Bundle().apply {
            putParcelable(argJobDetailName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_CAR_JOB_PICKER_KEY -> {
                val carJob: CarJob? = result.getParcelable(ARG_CAR_JOB)
                viewModel.jobDetail?.let { jobdet ->
                    jobdet.carJob = carJob
                    carJobTextView.text = carJob?.name ?: ""
                }
            }

            REQUEST_WORKING_HOUR_PICKER_KEY -> {
                val workingHour: WorkingHour? = result.getParcelable(ARG_WORKING_HOUR)
                viewModel.jobDetail?.let { jobdet ->
                    jobdet.workingHour = workingHour
                    workingHourTextView.text = workingHour?.name ?: ""
                    calculateSum()
                }
            }
        }
    }

    private fun validateInput(): ValidateInputResult {
        var result = ValidateInputResult(true, "")
        viewModel.jobDetail?.let { jobdet ->
            jobdet.carJob ?: let {
                result.isValid = false
                result.errorMessage = result.errorMessage + getString(R.string.error_empty_car_job) + "\n"
            }
            jobdet.workingHour ?: let {
                result.isValid = false
                result.errorMessage = result.errorMessage + getString(R.string.error_empty_working_hour) + "\n"
            }
        }
        return result
    }
}
