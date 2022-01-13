package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

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
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob.CarJobPickerFragment

class JobDetailFragment: DialogFragment(), FragmentResultListener {

    companion object {

        private const val JOB_DETAIL = "job_detail"
        private const val PARENT_REQUEST_KEY = "parent_request_job_detail_picker_key"
        private const val PARENT_JOB_DETAIL_ARG_NAME = "parent_job_detail_arg_name"

        private val REQUEST_CAR_JOB_PICKER_KEY = "request_car_job_picker_key"
        private val ARG_CAR_JOB = "car_job_picker"

        fun newInstance(jobDetail: JobDetail?): JobDetailFragment {
            val args = Bundle().apply {
                putParcelable(JOB_DETAIL, jobDetail)
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
    private lateinit var carJobTextView: TextView
    private lateinit var workingHourTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            jobDetail = arg.getParcelable(JOB_DETAIL)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argJobDetailName = arg.getString(PARENT_JOB_DETAIL_ARG_NAME, "")

        } ?: run {
            throw RuntimeException("There are not arguments in JobDetailFragment")
        }

        jobDetail?:let {
            throw RuntimeException("jobDetail is Null in JobDetailFragment")
        }

        viewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
        viewModel.jobDetail = jobDetail

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.job_detail_title)

        val container = layoutInflater.inflate(R.layout.fragment_job_detail, null, false)
        carJobSelectButton = container.findViewById(R.id.car_job_select_button)
        workingHourSelectButton = container.findViewById(R.id.working_hour_select_button)
        carJobTextView = container.findViewById(R.id.car_job_text_view)
        workingHourTextView = container.findViewById(R.id.working_hour_text_view)

        alertDialogBuilder.setView(container)

        setupClickListeners()

//        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
//            sendResultToFragment(null)
//        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            sendResultToFragment(jobDetail)
        }

        // viewLifecycleOwner - здесь не может быть получен, вместо него this использую
        childFragmentManager.setFragmentResultListener(REQUEST_CAR_JOB_PICKER_KEY, this, this)

        return alertDialogBuilder.create()
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
        // ...
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
        }
    }
}