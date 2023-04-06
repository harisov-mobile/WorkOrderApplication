package ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob

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
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.di.ViewModelFactory

class CarJobPickerFragment : DialogFragment() {

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var requestKey = ""
    private var argCarJobName = ""

    private lateinit var viewModel: CarJobListViewModel
    private lateinit var carJobListRecyclerView: RecyclerView
    private lateinit var carJobListAdapter: CarJobListAdapter

    private lateinit var clearSearchTextImageButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // даггер:
        component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CarJobListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedCarJob ?: let {
                viewModel.selectedCarJob = arg.getParcelable(CAR_JOB)
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argCarJobName = arg.getString(PARENT_CAR_JOB_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in CarJobPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        // alertDialogBuilder.setTitle(R.string.car_job_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        clearSearchTextImageButton = container.findViewById(R.id.clear_search_text_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        titleTextView = container.findViewById(R.id.title_text_view)
        titleTextView.text = getString(R.string.car_job_picker_title)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedCarJob)
        }

        setupCarJobListRecyclerView(container)

        viewModel.carJobListLiveData.observe(this, { carJobs ->
            carJobListAdapter = CarJobListAdapter(carJobs)
            carJobListRecyclerView.adapter = carJobListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedCarJob, carJobListAdapter.carJobs)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedCarJob = null
            } else {
                viewModel.selectedCarJob = carJobs[currentPosition]
                viewModel.selectedCarJob?.isSelected = true

                val scrollPosition = if (currentPosition > (carJobListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    carJobListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                carJobListRecyclerView.scrollToPosition(scrollPosition)
                carJobListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadCarJobList() // самое главное!!! если это создание нового фрагмента
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

    private fun setupCarJobListRecyclerView(view: View) {
        carJobListRecyclerView = view.findViewById(R.id.list_recycler_view)
        carJobListAdapter = CarJobListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        carJobListRecyclerView.adapter = carJobListAdapter
    }

    private fun setupClickListeners() {
        carJobListAdapter.onCarJobClickListener = { currentCarJob ->
            carJobListAdapter.carJobs.forEach {
                it.isSelected = false // снимем пометку у всех
            }
            viewModel.selectedCarJob = currentCarJob
            viewModel.selectedCarJob?.isSelected = true
        }

        carJobListAdapter.onCarJobLongClickListener = { currentCarJob ->
            sendResultToFragment(currentCarJob)
            dismiss()
        }

        clearSearchTextImageButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.loadCarJobList()
        }
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            viewModel.loadCarJobList()
        } else {
            viewModel.searchCarJobs(searchText)
        }
    }

    private fun getPosition(searchedCarJob: CarJob?, carJobList: List<CarJob>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedCarJob?.let {
            var isFound = false
            for (currentCarJob in carJobList) {
                currentPosition++
                if (currentCarJob.id == searchedCarJob.id) {
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

    private fun sendResultToFragment(result: CarJob?) {
        val bundle = Bundle().apply {
            putParcelable(argCarJobName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

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
}
