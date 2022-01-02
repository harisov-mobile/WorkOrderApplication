package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrderBinding
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.common.ScreenMode
import ru.internetcloud.workorderapplication.presentation.workorder.detail.car.CarPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.partner.PartnerPickerFragment
import java.util.*

class WorkOrderFragment : Fragment(), FragmentResultListener {

    private var _binding: FragmentWorkOrderBinding? = null
    private val binding: FragmentWorkOrderBinding
    get() = _binding ?: throw RuntimeException("Error FragmentWorkOrderBinding is NULL")

    private lateinit var viewModel: WorkOrderViewModel

    private var screenMode: ScreenMode? = null
    private var workOrderId: String? = null

    companion object {

        const val ARG_SCREEN_MODE = "screen_mode"
        const val ARG_WORK_ORDER_ID = "work_order_id"

        private val REQUEST_DATE_PICKER_KEY = "request_date_picker_key"
        private val ARG_DATE = "date_picker"

        private val REQUEST_PARTNER_PICKER_KEY = "request_partner_picker_key"
        private val ARG_PARTNER = "partner_picker"

        private val REQUEST_CAR_PICKER_KEY = "request_car_picker_key"
        private val ARG_CAR = "car_picker"

        fun newInstanceAddWorkOrder(): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCREEN_MODE, ScreenMode.ADD)
            instance.arguments = args
            return instance
        }

        fun newInstanceEditWorkOrder(workOrderId: String): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCREEN_MODE, ScreenMode.EDIT)
            args.putString(ARG_WORK_ORDER_ID, workOrderId)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkArgs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderViewModel::class.java)

        launchCorrectMode()

        observeViewModel()

        childFragmentManager.setFragmentResultListener(REQUEST_DATE_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_PARTNER_PICKER_KEY, viewLifecycleOwner, this)

        setupClickListeners()
    }

    private fun observeViewModel() {
        // подписка на ошибки
        viewModel.errorInputNumber.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_number)
            } else {
                null
            }
            binding.numberTextInputLayout.error = message
        }

        // подписка на успешное завершение сохранения
        viewModel.canFinish.observe(viewLifecycleOwner) {
            Toast.makeText(context, getString(R.string.success_saved), Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchCorrectMode() {
        when (screenMode) {
            ScreenMode.EDIT -> launchEditMode()
            ScreenMode.ADD -> launchAddMode()
        }
    }

    private fun checkArgs() {

        val args = requireArguments()
        if (!args.containsKey(ARG_SCREEN_MODE)) {
            throw RuntimeException("Parameter mode is absent")
        }

        val mode = args.getSerializable(ARG_SCREEN_MODE) as ScreenMode
        if (mode != ScreenMode.EDIT && mode != ScreenMode.ADD) {
            throw RuntimeException("Uknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == ScreenMode.EDIT) {
            workOrderId = args.getString(ARG_WORK_ORDER_ID)
        }
    }

    private fun launchEditMode() {
        workOrderId?.let {
            viewModel.loadWorkOrder(it)
        }

        viewModel.workOrder.observe(viewLifecycleOwner) {
            order ->
            binding.numberEditText.setText(order.number)
            binding.dateTextView.text = DateConverter.getDateString(order.date)
            // сюда добавить
            binding.requestReasonEditText.setText(order.requestReason)
            binding.commentEditText.setText(order.comment)
            binding.partnerTextView.text = order.partner?.name
            binding.carTextView.text = order.car?.name
        }

        binding.saveButton.setOnClickListener {
            setupFieldsToWorkOrder()
            viewModel.updateWorkOrder()
        }
    }

    private fun launchAddMode() {
        viewModel.createWorkOrder()

        binding.saveButton.setOnClickListener {
            setupFieldsToWorkOrder()
            viewModel.addWorkOrder()
        }
    }

    private fun setupFieldsToWorkOrder() {
        viewModel.workOrder.value?.number = parseText(binding.numberEditText.text?.toString())
        viewModel.workOrder.value?.requestReason = parseText(binding.requestReasonEditText.text?.toString())
        viewModel.workOrder.value?.comment = parseText(binding.commentEditText.text?.toString())
        viewModel.workOrder.value?.isModified
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
                viewModel.resetErrorInputNumber()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseText(inputText: String?): String {
        return inputText?.trim() ?: ""
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when(requestKey) {
            REQUEST_DATE_PICKER_KEY -> {
                val date = result.getSerializable(ARG_DATE) as Date
                binding.dateTextView.text = DateConverter.getDateString(date)
                viewModel.workOrder.value?.date = date
            }
            REQUEST_PARTNER_PICKER_KEY -> {
                val partner: Partner? = result.getParcelable<Partner>(ARG_PARTNER)
                viewModel.workOrder.value?.let { order ->
                    order.partner = partner
                    binding.partnerTextView.text = partner?.name ?:""
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.dateSelectButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                DatePickerFragment
                    .newInstance(order.date, REQUEST_DATE_PICKER_KEY, ARG_DATE)
                    .show(childFragmentManager, REQUEST_DATE_PICKER_KEY)
            }
        }

        binding.partnerSelectButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                order.partner?.isSelected = true

                PartnerPickerFragment
                    .newInstance(order.partner, REQUEST_PARTNER_PICKER_KEY, ARG_PARTNER)
                    .show(childFragmentManager, REQUEST_PARTNER_PICKER_KEY)
            }
        }

        binding.carSelectButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                order.car?.isSelected = true

                CarPickerFragment
                    .newInstance(order.car, REQUEST_CAR_PICKER_KEY, ARG_CAR)
                    .show(childFragmentManager, REQUEST_CAR_PICKER_KEY)
            }
        }
    }
}
