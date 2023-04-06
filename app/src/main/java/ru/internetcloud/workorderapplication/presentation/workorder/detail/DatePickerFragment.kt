package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val requestKey = arguments?.getString(ARG_PARENT_REQUEST_KEY, "")
        val argDateName = arguments?.getString(ARG_PARENT_ARG_DATE_NAME, "")

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val resultDate: Date = GregorianCalendar(year, month, day).time

            val bundle = Bundle().apply {
                putSerializable(argDateName, resultDate)
            }

            requestKey?.let {
                setFragmentResult(requestKey, bundle)
            }
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {

        private const val ARG_DATE = "date"
        private const val ARG_PARENT_REQUEST_KEY = "parent_request_date_picker_key"
        private const val ARG_PARENT_ARG_DATE_NAME = "parent_arg_date_name"

        fun newInstance(date: Date, parentRequestKey: String, parentArgDateName: String): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
                putString(ARG_PARENT_REQUEST_KEY, parentRequestKey)
                putString(ARG_PARENT_ARG_DATE_NAME, parentArgDateName)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}
