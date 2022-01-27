package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrderBinding
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.common.ScreenMode
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.car.CarPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails.JobDetailFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails.JobDetailListAdapter
import ru.internetcloud.workorderapplication.presentation.workorder.detail.partner.PartnerPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.performers.PerformerDetailFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.performers.PerformerDetailListAdapter
import ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype.RepairTypePickerFragment
import java.util.*

class WorkOrderFragment : Fragment(), FragmentResultListener {

    private var _binding: FragmentWorkOrderBinding? = null
    private val binding: FragmentWorkOrderBinding
        get() = _binding ?: throw RuntimeException("Error FragmentWorkOrderBinding is NULL")

    private lateinit var viewModel: WorkOrderViewModel
    private lateinit var performerDetailListAdapter: PerformerDetailListAdapter
    private lateinit var jobDetailListAdapter: JobDetailListAdapter

    private var screenMode: ScreenMode? = null
    private var workOrderId: String? = null

    private var modifyAllowed: Boolean = true

    companion object {

        const val ARG_SCREEN_MODE = "screen_mode"
        const val ARG_WORK_ORDER_ID = "work_order_id"

        private val REQUEST_DATE_PICKER_KEY = "request_date_picker_key"
        private val ARG_DATE = "date_picker"

        private val REQUEST_PARTNER_PICKER_KEY = "request_partner_picker_key"
        private val ARG_PARTNER = "partner_picker"

        private val REQUEST_CAR_PICKER_KEY = "request_car_picker_key"
        private val ARG_CAR = "car_picker"

        private val REQUEST_REPAIR_TYPE_PICKER_KEY = "request_repair_type_picker_key"
        private val ARG_REPAIR_TYPE = "repair_type_picker"

        private val REQUEST_DEPARTMENT_PICKER_KEY = "request_department_picker_key"
        private val ARG_DEPARTMENT = "department_picker"

        private val REQUEST_JOB_DETAIL_PICKER_KEY = "request_job_detail_picker_key"
        private val REQUEST_PERFORMER_DETAIL_PICKER_KEY = "request_performer_detail_picker_key"
        private val ARG_JOB_DETAIL = "job_detail_picker"
        private val ARG_PERFORMER_DETAIL = "performer_detail_picker"

        private val REQUEST_DATA_WAS_CHANGED_KEY = "data_was_changed_key"
        private val REQUEST_DELETE_JOB_DETAIL_KEY = "delete_job_detail_key"
        private val REQUEST_DELETE_PERFORMER_DETAIL_KEY = "delete_performer_detail_key"
        private val ARG_ANSWER = "answer"

        fun newInstanceAddWorkOrder(): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putParcelable(ARG_SCREEN_MODE, ScreenMode.ADD)
            instance.arguments = args
            return instance
        }

        fun newInstanceEditWorkOrder(workOrderId: String): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putParcelable(ARG_SCREEN_MODE, ScreenMode.EDIT)
            args.putString(ARG_WORK_ORDER_ID, workOrderId)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onCloseWorkOrder()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderViewModel::class.java)

        savedInstanceState?.let {
            viewModel.workOrder.value?.let { order ->
                updateUI(order)
            }
        } ?: let {
            checkArgs()
            // списки сначала инициализирую пустым списком, потом они заполнятся
            setupPerformerDetailListRecyclerView(emptyList())
            setupJobDetailListRecyclerView(emptyList())
            launchCorrectMode()
        }

        observeViewModel()

        childFragmentManager.setFragmentResultListener(REQUEST_DATE_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_PARTNER_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_CAR_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_REPAIR_TYPE_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_DEPARTMENT_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_JOB_DETAIL_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_PERFORMER_DETAIL_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_DATA_WAS_CHANGED_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_DELETE_JOB_DETAIL_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_DELETE_PERFORMER_DETAIL_KEY, viewLifecycleOwner, this)

        setupClickListeners()
    }

    private fun observeViewModel() {
        // подписка на ошибки
        viewModel.errorInputNumber.observe(viewLifecycleOwner) { isError ->
            val message = if (isError) {
                getString(R.string.error_input_number)
            } else {
                null
            }
            binding.numberTextInputLayout.error = message
        }

        viewModel.showErrorMessage.observe(viewLifecycleOwner) { show ->
            if (show) {
                var errorMessage = ""
                viewModel.errorInputNumber.value?.let { isError ->
                    if (isError) {
                        errorMessage = errorMessage + getString(R.string.error_input_number) + "\n"
                    }
                }

                if (viewModel.errorInputPerformer) {
                    errorMessage = errorMessage + getString(R.string.error_input_performer) + "\n"
                }

                viewModel.resetShowErrorMessage()

                MessageDialogFragment.newInstance(errorMessage)
                    .show(childFragmentManager, null)
            }
        }

        // подписка на успешное завершение сохранения
        viewModel.canFinish.observe(viewLifecycleOwner) {
            if (viewModel.closeOnSave) {
                Toast.makeText(context, getString(R.string.success_saved), Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.popBackStack()
            } else {
                viewModel.workOrder.value?.isModified = false
                MessageDialogFragment.newInstance(getString(R.string.success_saved))
                    .show(childFragmentManager, null)
            }
        }
    }

    private fun launchCorrectMode() {
        when (screenMode) {
            ScreenMode.EDIT -> launchEditMode()
            ScreenMode.ADD -> launchAddMode()
        }

        viewModel.workOrder.observe(viewLifecycleOwner) { order ->
            updateUI(order)
        }
    }

    private fun checkArgs() {

        val args = requireArguments()
        if (!args.containsKey(ARG_SCREEN_MODE)) {
            throw RuntimeException("Parameter mode is absent")
        }

        val mode = args.getParcelable<ScreenMode>(ARG_SCREEN_MODE)
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
    }

    private fun launchAddMode() {
        viewModel.createWorkOrder()
    }

    private fun setupPerformerDetailListRecyclerView(performerDetailList: List<PerformerDetail>) {
        performerDetailListAdapter = PerformerDetailListAdapter()
        performerDetailListAdapter.submitList(performerDetailList)
        binding.performerDetailsRecyclerView.adapter = performerDetailListAdapter

        performerDetailListAdapter.onPerformerDetailClickListener = { currentPerformerDetail ->
            viewModel.selectedPerformerDetail?.isSelected = false
            viewModel.selectedPerformerDetail = currentPerformerDetail
            viewModel.selectedPerformerDetail?.isSelected = true
        }
    }

    private fun setupJobDetailListRecyclerView(jobDetailList: List<JobDetail>) {
        jobDetailListAdapter = JobDetailListAdapter()
        jobDetailListAdapter.submitList(jobDetailList)
        binding.jobDetailsRecyclerView.adapter = jobDetailListAdapter

        jobDetailListAdapter.onJobDetailClickListener = { currentJobDetail ->
            viewModel.selectedJobDetail?.isSelected = false // предыдущую отмеченную строку работ сделаем неотмеченной
            viewModel.selectedJobDetail = currentJobDetail
            viewModel.selectedJobDetail?.isSelected = true
        }
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
                if (modifyAllowed) {
                    viewModel.workOrder.value?.number = parseText(p0?.toString())
                    viewModel.workOrder.value?.isModified = true
                }
            }
        })

        binding.mileageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (modifyAllowed) {
                    viewModel.workOrder.value?.mileage = parseNumber(p0?.toString()).toInt()
                    viewModel.workOrder.value?.isModified = true
                }
            }
        })

        binding.requestReasonEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun afterTextChanged(p0: Editable?) {
                if (modifyAllowed) {
                    viewModel.workOrder.value?.requestReason = parseText(p0?.toString())
                    viewModel.workOrder.value?.isModified = true
                }
            }
        })

        binding.commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (modifyAllowed) {
                    viewModel.workOrder.value?.comment = parseText(p0?.toString())
                    viewModel.workOrder.value?.isModified = true
                }
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

    private fun parseNumber(inputText: String?): String {
        var result = "0"
        inputText?.let {
            if (!it.isEmpty()) {
                result = it
            }
        }
        return result
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE_PICKER_KEY -> {
                val date = result.getSerializable(ARG_DATE) as Date
                viewModel.workOrder.value?.let { order ->
                    if (order.date != date) {
                        order.date = date
                        binding.dateTextView.text = DateConverter.getDateString(date)
                        order.isModified = true
                    }
                }
            }

            REQUEST_PARTNER_PICKER_KEY -> {
                val partner: Partner? = result.getParcelable(ARG_PARTNER)
                viewModel.workOrder.value?.let { order ->
                    if (order.partner != partner) {
                        order.partner = partner
                        binding.partnerTextView.text = partner?.name ?: ""
                        order.isModified = true

                        // т.к. изменили Заказчика надо очистить СХТ, .т.к. СХТ от другого заказчика
                        order.car = null
                        binding.carTextView.text = ""
                    }
                }
            }

            REQUEST_CAR_PICKER_KEY -> {
                val car: Car? = result.getParcelable(ARG_CAR)
                viewModel.workOrder.value?.let { order ->
                    if (order.car != car) {
                        order.car = car
                        binding.carTextView.text = car?.name ?: ""
                        order.isModified = true
                    }
                }
            }

            REQUEST_REPAIR_TYPE_PICKER_KEY -> {
                val repairType: RepairType? = result.getParcelable(ARG_REPAIR_TYPE)
                viewModel.workOrder.value?.let { order ->
                    if (order.repairType != repairType) {
                        order.repairType = repairType
                        binding.repairTypeTextView.text = repairType?.name ?: ""
                        order.isModified = true
                    }
                }
            }

            REQUEST_DEPARTMENT_PICKER_KEY -> {
                val department: Department? = result.getParcelable(ARG_DEPARTMENT)
                viewModel.workOrder.value?.let { order ->
                    if (order.department != department) {
                        order.department = department
                        binding.departmentTextView.text = department?.name ?: ""
                        order.isModified = true
                    }
                }
            }

            REQUEST_JOB_DETAIL_PICKER_KEY -> {
                val jobDetail: JobDetail? = result.getParcelable(ARG_JOB_DETAIL)
                jobDetail?.let { jobdet ->
                    viewModel.workOrder.value?.let { order ->
                        val foundJobDetail = order.jobDetails.find { it.lineNumber == jobdet.lineNumber }
                        foundJobDetail ?: let {
                            // это новая строка, добавленная в ТЧ
                            order.jobDetails.add(jobdet)
                            binding.jobDetailsRecyclerView.scrollToPosition(order.jobDetails.indexOf(jobdet))
                        }
                        order.jobDetails.forEach {
                            it.isSelected = false
                        }
                        viewModel.selectedJobDetail = jobdet
                        viewModel.selectedJobDetail?.isSelected = true

                        jobDetailListAdapter.notifyItemChanged(
                            order.jobDetails.indexOf(viewModel.selectedJobDetail),
                            Unit
                        )
                        order.isModified = true
                    }
                }
            }

            REQUEST_PERFORMER_DETAIL_PICKER_KEY -> {
                val performerDetail: PerformerDetail? = result.getParcelable(ARG_PERFORMER_DETAIL)
                performerDetail?.let { currentPerformerDetail ->
                    viewModel.workOrder.value?.let { order ->
                        val foundPerformerDetail = order.performers.find { it.lineNumber == currentPerformerDetail.lineNumber }
                        foundPerformerDetail ?: let {
                            // это новая строка, добавленная в ТЧ
                            order.performers.add(currentPerformerDetail)
                            binding.performerDetailsRecyclerView.scrollToPosition(order.performers.indexOf(currentPerformerDetail))
                        }
                        order.performers.forEach {
                            it.isSelected = false
                        }
                        viewModel.selectedPerformerDetail = currentPerformerDetail
                        viewModel.selectedPerformerDetail?.isSelected = true

                        performerDetailListAdapter.notifyItemChanged(
                            order.performers.indexOf(viewModel.selectedPerformerDetail),
                            Unit
                        )
                        order.isModified = true
                    }
                }
            }

            REQUEST_DATA_WAS_CHANGED_KEY -> {
                val needSaveData: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (needSaveData) {
                    viewModel.closeOnSave = true
                    viewModel.updateWorkOrder()
                } else {
                    activity?.supportFragmentManager?.popBackStack()
                }
            }

            REQUEST_DELETE_JOB_DETAIL_KEY -> {
                val delete: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (delete) {
                    viewModel.workOrder.value?.let { order ->
                        val removedPosition = order.jobDetails.indexOf(viewModel.selectedJobDetail)
                        order.jobDetails.remove(viewModel.selectedJobDetail)
                        viewModel.selectedJobDetail = null

                        var pos = 0
                        order.jobDetails.forEach {
                            pos++
                            it.lineNumber = pos
                        }

                        jobDetailListAdapter.notifyDataSetChanged()
                        order.isModified = true
                    }
                }
            }

            REQUEST_DELETE_PERFORMER_DETAIL_KEY -> {
                val delete: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (delete) {
                    viewModel.workOrder.value?.let { order ->
                        val removedPosition = order.performers.indexOf(viewModel.selectedPerformerDetail)
                        order.performers.remove(viewModel.selectedPerformerDetail)
                        viewModel.selectedPerformerDetail = null

                        var pos = 0
                        order.performers.forEach {
                            pos++
                            it.lineNumber = pos
                        }

                        performerDetailListAdapter.notifyDataSetChanged()
                        order.isModified = true
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {

        binding.saveButton.setOnClickListener {
            viewModel.updateWorkOrder()
        }

        binding.closeButton.setOnClickListener {
            onCloseWorkOrder()
        }

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

                order.partner?.let { partner ->
                    order.car?.isSelected = true

                    CarPickerFragment
                        .newInstance(order.car, partner, REQUEST_CAR_PICKER_KEY, ARG_CAR)
                        .show(childFragmentManager, REQUEST_CAR_PICKER_KEY)
                } ?: let {
                    MessageDialogFragment.newInstance(getString(R.string.error_specify_partner))
                        .show(childFragmentManager, null)
                }
            }
        }

        binding.repairTypeSelectButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                order.repairType?.isSelected = true

                RepairTypePickerFragment
                    .newInstance(order.repairType, REQUEST_REPAIR_TYPE_PICKER_KEY, ARG_REPAIR_TYPE)
                    .show(childFragmentManager, REQUEST_REPAIR_TYPE_PICKER_KEY)
            }
        }

        binding.departmentSelectButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                order.department?.isSelected = true

                DepartmentPickerFragment
                    .newInstance(order.department, REQUEST_DEPARTMENT_PICKER_KEY, ARG_DEPARTMENT)
                    .show(childFragmentManager, REQUEST_DEPARTMENT_PICKER_KEY)
            }
        }

        binding.addJobDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                JobDetailFragment
                    .newInstance(
                        getNewJobDetail(order),
                        REQUEST_JOB_DETAIL_PICKER_KEY,
                        ARG_JOB_DETAIL
                    ) // здесь надо подумать как правильно создавать новую строку ТЧ
                    .show(childFragmentManager, REQUEST_JOB_DETAIL_PICKER_KEY)
            }
        }

        binding.editJobDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                viewModel.selectedJobDetail?.let {
                    JobDetailFragment
                        .newInstance(it, REQUEST_JOB_DETAIL_PICKER_KEY, ARG_JOB_DETAIL)
                        .show(childFragmentManager, REQUEST_JOB_DETAIL_PICKER_KEY)
                } ?: run {
                    MessageDialogFragment.newInstance(getString(R.string.edit_job_detail_not_selected))
                        .show(childFragmentManager, null)
                }
            }
        }

        binding.deleteJobDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                viewModel.selectedJobDetail?.let {
                    QuestionDialogFragment
                        .newInstance(
                            getString(R.string.delete_job_detail_question),
                            REQUEST_DELETE_JOB_DETAIL_KEY,
                            ARG_ANSWER
                        )
                        .show(childFragmentManager, REQUEST_DELETE_JOB_DETAIL_KEY)
                } ?: run {
                    MessageDialogFragment.newInstance(getString(R.string.delete_job_detail_not_selected))
                        .show(childFragmentManager, null)
                }
            }
        }

        binding.mileageEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val currentValue = binding.mileageEditText.text.toString()
                if (currentValue.equals("0")) {
                    binding.mileageEditText.setText("")
                }
            }
        }

        binding.addPerformerDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                PerformerDetailFragment
                    .newInstance(
                        getNewPerformerDetail(order),
                        REQUEST_PERFORMER_DETAIL_PICKER_KEY,
                        ARG_PERFORMER_DETAIL
                    ) // здесь надо подумать как правильно создавать новую строку ТЧ
                    .show(childFragmentManager, REQUEST_PERFORMER_DETAIL_PICKER_KEY)
            }
        }

        binding.editPerformerDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                viewModel.selectedPerformerDetail?.let {
                    PerformerDetailFragment
                        .newInstance(it, REQUEST_PERFORMER_DETAIL_PICKER_KEY, ARG_PERFORMER_DETAIL)
                        .show(childFragmentManager, REQUEST_PERFORMER_DETAIL_PICKER_KEY)
                } ?: run {
                    MessageDialogFragment.newInstance(getString(R.string.edit_performer_detail_not_selected))
                        .show(childFragmentManager, null)
                }
            }
        }

        binding.deletePerformerDetailButton.setOnClickListener {
            viewModel.workOrder.value?.let { order ->
                viewModel.selectedPerformerDetail?.let {
                    QuestionDialogFragment
                        .newInstance(
                            getString(R.string.delete_performer_detail_question),
                            REQUEST_DELETE_PERFORMER_DETAIL_KEY,
                            ARG_ANSWER
                        )
                        .show(childFragmentManager, REQUEST_DELETE_PERFORMER_DETAIL_KEY)
                } ?: run {
                    MessageDialogFragment.newInstance(getString(R.string.delete_performer_detail_not_selected))
                        .show(childFragmentManager, null)
                }
            }
        }
    }

    private fun getNewJobDetail(order: WorkOrder): JobDetail {
        var lineNumber = order.jobDetails.size
        lineNumber++
        val id = order.id + "_" + lineNumber.toString()
        return JobDetail(id = id, lineNumber = lineNumber)
    }

    private fun getNewPerformerDetail(order: WorkOrder): PerformerDetail {
        var lineNumber = order.performers.size
        lineNumber++
        val id = order.id + "_" + lineNumber.toString()
        return PerformerDetail(id = id, lineNumber = lineNumber)
    }

    private fun updateUI(order: WorkOrder) {

        modifyAllowed = false

        binding.numberEditText.setText(order.number)
        binding.dateTextView.text = DateConverter.getDateString(order.date)
        binding.partnerTextView.text = order.partner?.name
        binding.carTextView.text = order.car?.name
        binding.repairTypeTextView.text = order.repairType?.name
        binding.departmentTextView.text = order.department?.name

        binding.mileageEditText.setText(order.mileage.toString())
        binding.requestReasonEditText.setText(order.requestReason)
        binding.commentEditText.setText(order.comment)

        // Табличная часть Исполнители:
        setupPerformerDetailListRecyclerView(order.performers)

        // Табличная часть Работы:
        setupJobDetailListRecyclerView(order.jobDetails)

        modifyAllowed = true

        if (order.posted) {
            context?.let {
                binding.mainContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.light_green_200))

                binding.numberEditText.setEnabled(false)
                binding.numberEditText.setTextColor(ContextCompat.getColor(it, R.color.gray_850))

                binding.mileageEditText.setEnabled(false)
                binding.mileageEditText.setTextColor(ContextCompat.getColor(it, R.color.gray_850))

                binding.requestReasonEditText.setEnabled(false)
                binding.requestReasonEditText.setTextColor(ContextCompat.getColor(it, R.color.gray_850))

                binding.commentEditText.setEnabled(false)
                binding.commentEditText.setTextColor(ContextCompat.getColor(it, R.color.gray_850))
            }

            binding.saveButton.visibility = View.INVISIBLE
            binding.dateSelectButton.visibility = View.INVISIBLE
            binding.partnerSelectButton.visibility = View.INVISIBLE
            binding.carSelectButton.visibility = View.INVISIBLE
            binding.repairTypeSelectButton.visibility = View.INVISIBLE
            binding.departmentSelectButton.visibility = View.INVISIBLE
            binding.addPerformerDetailButton.visibility = View.INVISIBLE
            binding.editPerformerDetailButton.visibility = View.INVISIBLE
            binding.deletePerformerDetailButton.visibility = View.INVISIBLE
            binding.addJobDetailButton.visibility = View.INVISIBLE
            binding.deleteJobDetailButton.visibility = View.INVISIBLE
            binding.editJobDetailButton.visibility = View.INVISIBLE
        }
    }

    private fun onCloseWorkOrder() {
        viewModel.workOrder.value?.let { order ->
            if (!order.posted && order.isModified) {
                QuestionDialogFragment
                    .newInstance(
                        getString(R.string.data_was_changed_question),
                        REQUEST_DATA_WAS_CHANGED_KEY,
                        ARG_ANSWER
                    )
                    .show(childFragmentManager, REQUEST_DATA_WAS_CHANGED_KEY)
            } else {
                activity?.supportFragmentManager?.popBackStack()
            }
        } ?: let {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}
