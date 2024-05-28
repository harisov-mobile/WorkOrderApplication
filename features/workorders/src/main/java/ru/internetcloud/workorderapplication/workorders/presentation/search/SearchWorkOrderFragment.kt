package ru.internetcloud.workorderapplication.workorders.presentation.search

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import java.util.Date
import ru.internetcloud.workorderapplication.common.domain.common.DateConverter
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.presentation.dialog.DatePickerFragment
import ru.internetcloud.workorderapplication.workorders.R
import ru.internetcloud.workorderapplication.workorders.databinding.FragmentWoFilterBinding

class SearchWorkOrderFragment : DialogFragment(), FragmentResultListener {

    private var requestKey = ""
    private var argSearchDataName = ""

    private lateinit var viewModel: SearchWorkOrderViewModel

    private var _binding: FragmentWoFilterBinding? = null
    private val binding: FragmentWoFilterBinding
        get() = _binding ?: throw RuntimeException("Error FragmentWoFilterBinding is NULL")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this).get(SearchWorkOrderViewModel::class.java)

        arguments?.let { arg ->
            savedInstanceState ?: let {
                val searchWorkOrderData: SearchWorkOrderData? = arg.getParcelable(SEARCH_DATA)
                searchWorkOrderData ?: let {
                    throw RuntimeException("searchWorkOrderData is Null in SearchWorkOrderFragment")
                }

                viewModel.searchWorkOrderData =
                    searchWorkOrderData.copy() // надо копию экземпляра класса, специально чтобы при изменении
                // копии не пострадал оригинал, если пользователь нажмет "Отмена"
            }

            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argSearchDataName = arg.getString(PARENT_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in SearchWorkOrderFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        // я использую свой заголовок, поэтому стандартный заголовок не нужен:
        // alertDialogBuilder.setTitle(R.string.job_detail_title)

        _binding = FragmentWoFilterBinding.inflate(LayoutInflater.from(context))

        binding.titleTextView.text = getString(R.string.filter_fragment_title)

        alertDialogBuilder.setView(binding.root)

        setupViews()

        setupClickListeners()

        // viewLifecycleOwner - здесь не может быть получен, вместо него this использую
        childFragmentManager.setFragmentResultListener(REQUEST_DATE_FROM_PICKER_KEY, this, this)
        childFragmentManager.setFragmentResultListener(REQUEST_DATE_TO_PICKER_KEY, this, this)

        return alertDialogBuilder.create()
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        binding.numberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchWorkOrderData?.numberText = parseText(p0?.toString())
            }
        })

        binding.partnerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchWorkOrderData?.partnerText = parseText(p0?.toString())
            }
        })

        binding.carEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchWorkOrderData?.carText = parseText(p0?.toString())
            }
        })

        binding.performerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchWorkOrderData?.performerText = parseText(p0?.toString())
            }
        })

        binding.departmentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchWorkOrderData?.departmentText = parseText(p0?.toString())
            }
        })
    }

    private fun setupViews() {
        viewModel.searchWorkOrderData?.let { data ->
            binding.dateFromTextView.text = DateConverter.getDateString(data.dateFrom)
            binding.dateToTextView.text = DateConverter.getDateString(data.dateTo)

            binding.numberEditText.setText(data.numberText)
            binding.partnerEditText.setText(data.partnerText)
            binding.carEditText.setText(data.carText)
            binding.performerEditText.setText(data.performerText)
            binding.departmentEditText.setText(data.departmentText)
        }
    }

    private fun setupClickListeners() {
        binding.clearNumberButton.setOnClickListener {
            viewModel.searchWorkOrderData?.numberText = ""
            binding.numberEditText.setText("")
        }

        binding.clearPartnerButton.setOnClickListener {
            viewModel.searchWorkOrderData?.partnerText = ""
            binding.partnerEditText.setText("")
        }

        binding.clearCarButton.setOnClickListener {
            viewModel.searchWorkOrderData?.carText = ""
            binding.carEditText.setText("")
        }

        binding.clearPerformerButton.setOnClickListener {
            viewModel.searchWorkOrderData?.performerText = ""
            binding.performerEditText.setText("")
        }

        binding.clearDepartmentButton.setOnClickListener {
            viewModel.searchWorkOrderData?.departmentText = ""
            binding.departmentEditText.setText("")
        }

        binding.clearDateFromButton.setOnClickListener {
            viewModel.searchWorkOrderData?.dateFrom = null
            binding.dateFromTextView.text = ""
        }

        binding.clearDateToButton.setOnClickListener {
            viewModel.searchWorkOrderData?.dateTo = null
            binding.dateToTextView.text = ""
        }

        binding.dateFromSelectButton.setOnClickListener {
            viewModel.searchWorkOrderData?.let { searchData ->

                val tempDate = searchData.dateFrom ?: Date()

                DatePickerFragment
                    .newInstance(tempDate, REQUEST_DATE_FROM_PICKER_KEY, ARG_DATE_FROM)
                    .show(childFragmentManager, REQUEST_DATE_FROM_PICKER_KEY)
            }
        }

        binding.dateToSelectButton.setOnClickListener {
            viewModel.searchWorkOrderData?.let { searchData ->

                val tempDate = searchData.dateTo ?: Date()

                DatePickerFragment
                    .newInstance(tempDate, REQUEST_DATE_TO_PICKER_KEY, ARG_DATE_TO)
                    .show(childFragmentManager, REQUEST_DATE_TO_PICKER_KEY)
            }
        }

        // Ok
        binding.okButton.setOnClickListener {
            sendResultToFragment(viewModel.searchWorkOrderData)
            dialog?.dismiss()
        }

        // Cancel
        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun sendResultToFragment(result: SearchWorkOrderData?) {
        // отправка установленного фильтра в родительский фрагмент: WorkOrderListFragment
        val bundle = Bundle().apply {
            putParcelable(argSearchDataName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        // получение из дочерних фрагментов выбранные даты

        when (requestKey) {
            REQUEST_DATE_FROM_PICKER_KEY -> {
                val date = result.getSerializable(ARG_DATE_FROM) as Date
                viewModel.searchWorkOrderData?.let { searchData ->
                    if (searchData.dateFrom != date) {
                        searchData.dateFrom = date
                        binding.dateFromTextView.text = DateConverter.getDateString(date)
                    }
                }
            }

            REQUEST_DATE_TO_PICKER_KEY -> {
                val date = result.getSerializable(ARG_DATE_TO) as Date
                viewModel.searchWorkOrderData?.let { searchData ->
                    if (searchData.dateTo != date) {
                        searchData.dateTo = date
                        binding.dateToTextView.text = DateConverter.getDateString(date)
                    }
                }
            }
        }
    }

    private fun parseText(inputText: String?): String {
        return inputText?.trim() ?: ""
    }

    companion object {

        private const val SEARCH_DATA = "search_data"
        private const val PARENT_REQUEST_KEY = "parent_request_picker_key"
        private const val PARENT_ARG_NAME = "parent_arg_name"

        private val REQUEST_DATE_FROM_PICKER_KEY = "request_date_from_picker_key"
        private val ARG_DATE_FROM = "date_from_picker"

        private val REQUEST_DATE_TO_PICKER_KEY = "request_date_to_picker_key"
        private val ARG_DATE_TO = "date_to_picker"

        fun newInstance(
            searchWorkOrderData: SearchWorkOrderData,
            parentRequestKey: String,
            parentArgName: String
        ): SearchWorkOrderFragment {
            val args = Bundle().apply {
                putParcelable(SEARCH_DATA, searchWorkOrderData)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_ARG_NAME, parentArgName)
            }
            return SearchWorkOrderFragment().apply {
                arguments = args
            }
        }
    }
}
