package ru.internetcloud.workorderapplication.presentation.workorder.detail.car

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
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class CarPickerFragment : DialogFragment() {

    companion object {

        private const val CAR = "car"
        private const val PARTNER = "partner"
        private const val PARENT_REQUEST_KEY = "parent_request_car_picker_key"
        private const val PARENT_CAR_ARG_NAME = "parent_car_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(car: Car?, partner: Partner, parentRequestKey: String, parentArgDateName: String): CarPickerFragment {
            val args = Bundle().apply {
                putParcelable(CAR, car)
                putParcelable(PARTNER, partner)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_CAR_ARG_NAME, parentArgDateName)
            }
            return CarPickerFragment().apply {
                arguments = args
            }
        }
    }

    private var requestKey = ""
    private var argCarName = ""

    private lateinit var viewModel: CarListViewModel
    private lateinit var carListRecyclerView: RecyclerView
    private lateinit var carListAdapter: CarListAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this).get(CarListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedCar ?: let {
                viewModel.selectedCar = arg.getParcelable(CAR)
            }

            viewModel.partner ?: let {
                viewModel.partner = arg.getParcelable(PARTNER) ?: throw RuntimeException("There is no partner in arg")
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argCarName = arg.getString(PARENT_CAR_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in CarPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.car_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        searchButton = container.findViewById(R.id.search_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedCar)
        }

        setupCarListRecyclerView(container)

        viewModel.carListLiveData.observe(this, { cars ->
            carListAdapter = CarListAdapter(cars)
            carListRecyclerView.adapter = carListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedCar, cars)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedCar = null
            } else {
                viewModel.selectedCar = cars[currentPosition]
                viewModel.selectedCar?.isSelected = true

                val scrollPosition = if (currentPosition > (carListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    carListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                carListRecyclerView.scrollToPosition(scrollPosition)
                carListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadCarList() // самое главное!!! если это создание нового фрагмента
        }

        return alertDialogBuilder.create()
    }

    private fun setupCarListRecyclerView(view: View) {
        carListRecyclerView = view.findViewById(R.id.list_recycler_view)
        carListAdapter = CarListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        carListRecyclerView.adapter = carListAdapter
    }

    private fun setupClickListeners() {
        carListAdapter.onCarClickListener = { currentCar ->
            carListAdapter.cars.forEach {
                it.isSelected = false // снимем пометку у всех
            }
            viewModel.selectedCar = currentCar
            viewModel.selectedCar?.isSelected = true
        }

        carListAdapter.onCarLongClickListener = { currentCar ->
            sendResultToFragment(currentCar)
            dismiss()
        }

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            if (searchText.isEmpty()) {
                viewModel.loadCarList()
            } else {
                viewModel.partner?.let { partner ->
                    viewModel.searchCarsByOwner(searchText, partner.id)
                }
            }
        }
    }

    private fun sendResultToFragment(result: Car?) {
        val bundle = Bundle().apply {
            putParcelable(argCarName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

    private fun getPosition(searchedCar: Car?, carList: List<Car>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedCar?.let {
            var isFound = false
            for (currentCar in carList) {
                currentPosition++
                if (currentCar.id == searchedCar.id) {
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
}
