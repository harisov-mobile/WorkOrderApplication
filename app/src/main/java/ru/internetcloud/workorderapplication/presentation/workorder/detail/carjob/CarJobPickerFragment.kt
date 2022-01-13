package ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.catalog.CarJob

class CarJobPickerFragment : DialogFragment() {

    companion object {

        private const val CAR_JOB = "car_job"
        private const val PARENT_REQUEST_KEY = "parent_request_car_job_picker_key"
        private const val PARENT_CAR_JOB_ARG_NAME = "parent_car_job_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(carJob: CarJob?, parentRequestKey: String, parentArgDateName: String): CarJobPickerFragment {
            val args = Bundle().apply {
                putParcelable(CAR_JOB, carJob)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_CAR_JOB_ARG_NAME, parentArgDateName)
            }
            return CarJobPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var carJob: CarJob? = null
    private var previousSelectedCarJob: CarJob? = null

    private var requestKey = ""
    private var argCarJobName = ""

    private lateinit var viewModel: CarJobListViewModel
    private lateinit var carJobListRecyclerView: RecyclerView
    private lateinit var carJobListAdapter: CarJobListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            carJob = arg.getParcelable(CAR_JOB)
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argCarJobName = arg.getString(PARENT_CAR_JOB_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in CarJobPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.car_job_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.button_clear) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok) { dialog, which ->
            sendResultToFragment(carJob)
        }

        setupCarJobListRecyclerView(container)

        viewModel = ViewModelProvider(this).get(CarJobListViewModel::class.java)
        viewModel.carJobListLiveData.observe(this, { partners ->
            carJobListAdapter = CarJobListAdapter(partners)
            carJobListRecyclerView.adapter = carJobListAdapter
            setupClickListeners()

            val currentPosition = getCurrentPosition(carJob)

            val scrollPosition = if (currentPosition > (carJobListAdapter.getItemCount() - DIFFERENCE_POS)) {
                carJobListAdapter.getItemCount() - 1
            } else {
                currentPosition
            }

            carJobListRecyclerView.scrollToPosition(scrollPosition)

            if (currentPosition != NOT_FOUND_POSITION) {
                partners[currentPosition].isSelected = true
                carJobListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        viewModel.loadCarJobList() // самое главное!!!

        return alertDialogBuilder.create()
    }

    private fun setupCarJobListRecyclerView(view: View) {
        carJobListRecyclerView = view.findViewById(R.id.list_recycler_view)
        carJobListAdapter = CarJobListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        carJobListRecyclerView.adapter = carJobListAdapter
    }

    private fun setupClickListeners() {
        carJobListAdapter.onCarJobClickListener = { currentCarJob ->
            carJob = currentCarJob
            previousSelectedCarJob?.isSelected = false
            carJob?.isSelected = true
            previousSelectedCarJob = carJob
        }

        carJobListAdapter.onCarJobLongClickListener = { currentCarJob ->
            sendResultToFragment(currentCarJob)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadCarJobList()
            } else {
                viewModel.searchCarJobs(searchText)
            }
        }
    }

    private fun getCurrentPosition(searchedCarJob: CarJob?): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedCarJob?.let {
            var isFound = false
            for (currentCarJob in carJobListAdapter.carJobs) {
                currentPosition++
                if (currentCarJob.id == searchedCarJob.id) {
                    isFound = true
                    previousSelectedCarJob = currentCarJob
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }

    private fun sendResultToFragment(result: CarJob?) {
        val bundle = Bundle().apply {
            putParcelable(argCarJobName, result)
        }
        setFragmentResult(requestKey, bundle)
    }
}
