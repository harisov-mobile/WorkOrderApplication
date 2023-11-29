package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.Date
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrder2Binding
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.model.document.JobDetail
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.presentation.sendemail.SendWorkOrderByIdToEmailDialogFragment
import ru.internetcloud.workorderapplication.presentation.util.convertToString
import ru.internetcloud.workorderapplication.presentation.util.launchAndCollectIn
import ru.internetcloud.workorderapplication.presentation.util.toIntOrDefault
import ru.internetcloud.workorderapplication.presentation.workorder.detail.car.CarPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.employee.EmployeePickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails.JobDetailFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails.JobDetailListAdapter
import ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails.JobDetailListListener
import ru.internetcloud.workorderapplication.presentation.workorder.detail.partner.PartnerPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.performers.PerformerDetailFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.performers.PerformerDetailListAdapter
import ru.internetcloud.workorderapplication.presentation.workorder.detail.performers.PerformerDetailListListener
import ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype.RepairTypePickerFragment

@AndroidEntryPoint
class WorkOrderFragment : Fragment(R.layout.fragment_work_order2), FragmentResultListener {

    private val binding by viewBinding(FragmentWorkOrder2Binding::bind)
    private val args by navArgs<WorkOrderFragmentArgs>()
    private val viewModel by viewModels<WorkOrderViewModel>()

    // эти переменные нужны для возврата во фрагмент со списком : надо ли делать скролл
    private val requestKeyReturnResult: String by lazy {
        args.requestKeyReturnResult
    }
    private val argNameReturnResult: String by lazy {
        args.argNameReturnResult
    }

    private var editTextInitialisation: Boolean = false

    // согласно моему открытию: Не надо самому вмешиваться в позиционирование списка после удаления!
    // RecyclerView и Adapter сами знают, что надо делать!
    // Надо просто не занулять адаптер
    private val performerDetailListAdapter: PerformerDetailListAdapter by lazy {
        // сначала - обработчик нажатий на элемент списка:
        val performerDetailListListener = object : PerformerDetailListListener {
            override fun onClickPerformerDetail(performerDetail: PerformerDetail) {
                viewModel.handleEvent(WorkOrderDetailEvent.OnSelectPerformerChange(performerDetail))
            }
        }
        // потом - создаем адаптер, который не будет зануляться при
        // жонглировании фрагментами
        PerformerDetailListAdapter(performerDetailListListener)
    }

    private val jobDetailListAdapter: JobDetailListAdapter by lazy {
        // сначала - обработчик нажатий на элемент списка:
        val jobDetailListListener = object : JobDetailListListener {
            override fun onClickJobDetail(jobDetail: JobDetail) {
                viewModel.handleEvent(WorkOrderDetailEvent.OnSelectJobChange(jobDetail))
            }
        }
        // потом - создаем адаптер, который не будет зануляться при
        // жонглировании фрагментами
        JobDetailListAdapter(jobDetailListListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // адаптер в binding занулять, так как
        //        В книге "Real World" на стр. 133 занулению адаптера посвящен абзац:
        //        Having an Adapter as a property of a Fragment is a known way of
        //        leaking the RecyclerView.
        //        That’s because, when the View is destroyed, the RecyclerView is destroyed
        //        along with it. But if the Fragment references the Adapter, the garbage
        //        collector won’t be able to collect the RecyclerView instance because
        //                Adapter s and RecyclerViews have a circular dependency. In other words,
        //        they reference each other.
        binding.performerDetailsRecyclerView.adapter = null
        binding.jobDetailsRecyclerView.adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPerformerDetailListRecyclerView()
        setupJobDetailListRecyclerView()
        setupClickListeners()
        interceptExit() // перехват нажатия кнопки Back
        setupFragmentResultListeners()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        // doAfterTextChanged нужно навешивать здесь, а не в onCreateView или onViewCreated, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз когда ОС Андроид сама восстановит состояние EditText
        setupOnTextChangedListeners()
    }

    private fun interceptExit() {
        // перехват нажатия кнопки "Back"
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onCloseWorkOrder()
                }
            }
        )
    }

    private fun onCloseWorkOrder() {
        // перехват нажатия кнопки "Back"
        if (viewModel.screenState.value.saving) {
            // ради чего все затевалось - это блокирование кнопки "Back"
            // пока не закончится запись
            // причина: если успеть нажать BACK то ВьюМодель уничтожится и отменится корутина, которая записывает
            // а если отменится корутина то запись произведена не будет.
        } else {
            if (viewModel.screenState.value.isModified) { // подумай над if (!order.posted && ...
                QuestionDialogFragment
                    .newInstance(
                        getString(R.string.data_was_changed_question),
                        REQUEST_KEY_DATA_WAS_CHANGED,
                        ANSWER_ARG_NAME_DATA_WAS_CHANGED
                    )
                    .show(childFragmentManager, REQUEST_KEY_DATA_WAS_CHANGED)
            } else {
                // можно смело выходить, так как или ничего не поменялось,
                // или было нажатие кнопки "save" и все уже сохранено
                sendResultToFragment()
                findNavController().popBackStack()
            }
        }
    }

    private fun setupFragmentResultListeners() {
        // чтобы получать от дочерних фрагментов информацию (диалоговое окно - "Данные изменились. Записать?")
        childFragmentManager.setFragmentResultListener(REQUEST_KEY_DATA_WAS_CHANGED, viewLifecycleOwner, this)

        // чтобы получить от дочерних фрагментов информацию об изменении: Даты, Заказчика, Автомобиля и т.д.
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
        childFragmentManager.setFragmentResultListener(REQUEST_MASTER_PICKER_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_ADD_DEFAULT_JOBS_KEY, viewLifecycleOwner, this)
    }

    private fun observeViewModel() {
        viewModel.screenState.launchAndCollectIn(viewLifecycleOwner) { currentState ->
            if (currentState.shouldCloseScreen) {
                Toast.makeText(context, getString(R.string.success_saved), Toast.LENGTH_SHORT).show()
                sendResultToFragment()
                findNavController().popBackStack()
            } else {
                if (currentState.canFillDefaultJobs) {
                    // спросить надо ли добавить в ТЧ "Работы":
                    QuestionDialogFragment
                        .newInstance(
                            getString(R.string.add_default_jobs_question),
                            REQUEST_ADD_DEFAULT_JOBS_KEY,
                            ARG_ANSWER
                        )
                        .show(childFragmentManager, REQUEST_ADD_DEFAULT_JOBS_KEY)
                    viewModel.handleEvent(WorkOrderDetailEvent.OnResetFillDefaultJobs)
                } else {
                    if (currentState.loading) {
                        renderWorkOrderViews(visible = false, posted = currentState.workOrder.posted)
                        renderErrorViews(visible = false, errorMessage = null)
                        renderLoadingViews(visible = true)
                    } else {
                        if (currentState.error != null) {
                            renderWorkOrderViews(visible = false, posted = currentState.workOrder.posted)
                            renderLoadingViews(visible = false)
                            renderErrorViews(visible = true, errorMessage = currentState.error.message)
                        } else {
                            renderLoadingViews(visible = false)
                            renderErrorViews(visible = false, errorMessage = null)
                            renderWorkOrderViews(
                                visible = true,
                                posted = currentState.workOrder.posted,
                                saving = currentState.saving
                            )
                            showWorkOrderValues(currentState)
                        }
                    }
                }
            }
        }

        viewModel.screenEventFlow.launchAndCollectIn(viewLifecycleOwner) { event ->
            when (event) {
                is WorkOrderDetailScreenEvent.ShowMessage -> {
                    val snackBar = Snackbar.make(
                        binding.root,
                        event.message,
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction("OK") {
                        snackBar.dismiss() // если не исчезает - вызови dismiss()
                    }
                    snackBar.show()
                }

                is WorkOrderDetailScreenEvent.ShowFieldsError -> {
                    // сборная солянка из сообщений об ошибках:
                    var errorMessage = ""

                    if (event.errorInputNumber) {
                        errorMessage = errorMessage + getString(R.string.error_input_number) + "\n"
                    }

                    // тут еще должны добавиться сообщения о других ошибках....
                    if (event.errorInputPerformer) {
                        errorMessage = errorMessage + getString(R.string.error_input_performer) + "\n"
                    }

                    MessageDialogFragment.newInstance(errorMessage)
                        .show(childFragmentManager, null)
                }

                is WorkOrderDetailScreenEvent.ShowSavingSuccess -> {
                    MessageDialogFragment.newInstance(getString(R.string.success_saved))
                        .show(childFragmentManager, null)
                }
            }
        }
    }

    private fun renderLoadingViews(visible: Boolean) {
        setMenuVisibility(false)

        with(binding) {
            loadingContainer.isVisible = visible
        }
    }

    private fun renderErrorViews(visible: Boolean, errorMessage: String? = null) {
        setMenuVisibility(false)
        with(binding) {
            errorTextView.text = errorMessage
            errorContainer.isVisible = visible
        }
    }

    private fun renderWorkOrderViews(
        visible: Boolean,
        posted: Boolean, // проведен или не проведен Заказ-наряд
        saving: Boolean = false
    ) {
        with(binding) {
            if (visible) {
                mainContainer.isVisible = true
                numberTextInputLayout.isEnabled = !saving && !posted
                mileageTextInputLayout.isEnabled = !saving && !posted
                requestReasonTextInputLayout.isEnabled = !saving && !posted
                commentTextInputLayout.isEnabled = !saving && !posted
                emailTextInputLayout.isEnabled = !saving

                if (posted) {
                    context?.let { currentContext ->
                        val grayColor = ContextCompat.getColor(currentContext, R.color.gray_850)
                        mainContainer.setBackgroundColor(
                            ContextCompat.getColor(
                                currentContext,
                                R.color.light_green_200
                            )
                        )
                        numberEditText.setTextColor(grayColor)
                        mileageEditText.setTextColor(grayColor)
                        requestReasonEditText.setTextColor(grayColor)
                        commentEditText.setTextColor(grayColor)
                    }
                } else {
                    saveButton.isVisible = true
                    saveButton.isEnabled = !saving
                    savingProgressBar.isVisible = saving

                    closeButton.isEnabled = !saving

                    dateSelectButton.isVisible = true
                    dateSelectButton.isEnabled = !saving

                    partnerSelectButton.isVisible = true
                    partnerSelectButton.isEnabled = !saving

                    carSelectButton.isVisible = true
                    carSelectButton.isEnabled = !saving

                    repairTypeSelectButton.isVisible = true
                    repairTypeSelectButton.isEnabled = !saving

                    departmentSelectButton.isVisible = true
                    departmentSelectButton.isEnabled = !saving

                    addPerformerDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE
                    editPerformerDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE
                    deletePerformerDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE

                    addJobDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE
                    deleteJobDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE
                    editJobDetailButton.visibility = if (!saving) View.VISIBLE else View.INVISIBLE

                    masterSelectButton.isVisible = true
                    masterSelectButton.isEnabled = !saving

                    sendToEmailButton.isEnabled = !saving
                }
            } else {
                mainContainer.isVisible = false
            }
        }
    }

    private fun showWorkOrderValues(state: UiWorkOrderDetailState) {
        with(binding) {
            dateTextView.text = DateConverter.getDateString(state.workOrder.date)
            partnerTextView.text = state.workOrder.partner?.name
            carTextView.text = state.workOrder.car?.name
            repairTypeTextView.text = state.workOrder.repairType?.name
            departmentTextView.text = state.workOrder.department?.name
            masterTextView.text = state.workOrder.master?.name

            // чтобы "doAfterTextChanged" не дергались и не зацикливалось приложение
            if (!state.workOrder.number.equals(numberEditText.text.toString())) {
                // не дергайся
                editTextInitialisation = true
                numberEditText.setText(state.workOrder.number)
                // дергайся
                editTextInitialisation = false
            }

            if (!state.workOrder.mileage.convertToString().equals(mileageEditText.text.toString())) {
                // не дергайся
                editTextInitialisation = true
                mileageEditText.setText(state.workOrder.mileage.convertToString())
                // дергайся
                editTextInitialisation = false
            }

            if (!state.workOrder.requestReason.equals(requestReasonEditText.text.toString())) {
                // не дергайся
                editTextInitialisation = true
                requestReasonEditText.setText(state.workOrder.requestReason)
                // дергайся
                editTextInitialisation = false
            }

            if (!state.workOrder.comment.equals(commentEditText.text.toString())) {
                // не дергайся
                editTextInitialisation = true
                commentEditText.setText(state.workOrder.comment)
                // дергайся
                editTextInitialisation = false
            }

            numberTextInputLayout.error = if (state.errorInputNumber) {
                getString(R.string.error_input_number)
            } else {
                null
            }

            emailTextInputLayout.error = if (state.errorInputEmail) {
                getString(R.string.wrong_email)
            } else {
                null
            }

            performerDetailListAdapter.submitList(state.workOrder.performers) {
                state.selectedPerformerDetail?.let { selectedPerformerDetail ->
                    binding.performerDetailsRecyclerView.scrollToPosition(
                        state.workOrder.performers.indexOf(
                            selectedPerformerDetail
                        )
                    )
                }
            } // Табличная часть Исполнители:

            jobDetailListAdapter.submitList(state.workOrder.jobDetails) {
                state.selectedJobDetail?.let { selectedJobDetail ->
                    binding.jobDetailsRecyclerView.scrollToPosition(
                        state.workOrder.jobDetails.indexOf(
                            selectedJobDetail
                        )
                    )
                }
            } // Табличная часть Работы:

            refreshTotalSum()
        }
    }

    private fun setupPerformerDetailListRecyclerView() {
        binding.performerDetailsRecyclerView.adapter = performerDetailListAdapter
        binding.performerDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupJobDetailListRecyclerView() {
        binding.jobDetailsRecyclerView.adapter = jobDetailListAdapter
        binding.jobDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupOnTextChangedListeners() {
        binding.numberEditText.doAfterTextChanged { text ->
            if (!editTextInitialisation) {
                viewModel.handleEvent(WorkOrderDetailEvent.OnNumberChange(number = text.toString()))
            }
        }

        binding.emailEditText.doAfterTextChanged { text ->
            viewModel.handleEvent(WorkOrderDetailEvent.OnEmailChange(email = text.toString()))
        }

        binding.mileageEditText.doAfterTextChanged { text ->
            if (!editTextInitialisation) {
                viewModel.handleEvent(
                    WorkOrderDetailEvent.OnMileageChange(
                        mileage = text.toString().toIntOrDefault()
                    )
                )
            }
        }

        binding.requestReasonEditText.doAfterTextChanged { text ->
            if (!editTextInitialisation) {
                viewModel.handleEvent(
                    WorkOrderDetailEvent.OnRequestReasonChange(
                        requestReason = text.toString()
                    )
                )
            }
        }

        binding.commentEditText.doAfterTextChanged { text ->
            if (!editTextInitialisation) {
                viewModel.handleEvent(
                    WorkOrderDetailEvent.OnCommentChange(
                        comment = text.toString()
                    )
                )
            }
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            // выбрали новую Дату заказ-наряда
            REQUEST_DATE_PICKER_KEY -> {
                val date = result.getSerializable(ARG_DATE) as Date
                viewModel.handleEvent(WorkOrderDetailEvent.OnDateChange(date = date))
            }

            // Выбрали Заказчика:
            REQUEST_PARTNER_PICKER_KEY -> {
                val partner: Partner? = result.getParcelable(ARG_PARTNER)
                viewModel.handleEvent(WorkOrderDetailEvent.OnPartnerChange(partner = partner))
            }

            // выбрали Автомобиль (СХТ):
            REQUEST_CAR_PICKER_KEY -> {
                val car: Car? = result.getParcelable(ARG_CAR)
                viewModel.handleEvent(WorkOrderDetailEvent.OnCarChange(car = car))
            }

            // выбрали Вид ремонта:
            REQUEST_REPAIR_TYPE_PICKER_KEY -> {
                val repairType: RepairType? = result.getParcelable(ARG_REPAIR_TYPE)
                viewModel.handleEvent(WorkOrderDetailEvent.OnRepairTypeChange(repairType = repairType))
            }

            // после ответа на вопрос: "Заполнить работы по-умолчанию?"
            REQUEST_ADD_DEFAULT_JOBS_KEY -> {
                val needAddDefaultJobs: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (needAddDefaultJobs) {
                    viewModel.handleEvent(WorkOrderDetailEvent.OnFillDefaultJobs)
                    binding.jobDetailsRecyclerView.scrollToPosition(
                        viewModel.screenState.value.workOrder.jobDetails.size - 1
                    )
                }
            }

            // выбрали Цех:
            REQUEST_DEPARTMENT_PICKER_KEY -> {
                val department: Department? = result.getParcelable(ARG_DEPARTMENT)
                viewModel.handleEvent(WorkOrderDetailEvent.OnDepartmentChange(department = department))
            }

            // выбрали Бригадира:
            REQUEST_MASTER_PICKER_KEY -> {
                val master: Employee? = result.getParcelable(ARG_MASTER)
                viewModel.handleEvent(WorkOrderDetailEvent.OnMasterChange(master = master))
            }

            // ввели строку ТЧ "Работы":
            REQUEST_JOB_DETAIL_PICKER_KEY -> {
                val jobDetail: JobDetail? = result.getParcelable(ARG_JOB_DETAIL)
                viewModel.handleEvent(WorkOrderDetailEvent.OnJobDetailChange(jobDetail = jobDetail))
                jobDetailListAdapter.notifyItemChanged(
                    viewModel.screenState.value.workOrder.jobDetails.indexOf(jobDetail),
                    Unit
                )
                refreshTotalSum()
            }

            // ввели строку ТЧ "Исполнители":
            REQUEST_PERFORMER_DETAIL_PICKER_KEY -> {
                val performerDetail: PerformerDetail? = result.getParcelable(ARG_PERFORMER_DETAIL)
                viewModel.handleEvent(WorkOrderDetailEvent.OnPerformerDetailChange(performerDetail = performerDetail))
                performerDetailListAdapter.notifyItemChanged(
                    viewModel.screenState.value.workOrder.performers.indexOf(performerDetail),
                    Unit
                )
            }

            // ответ на вопрос: "Записать данные?"
            REQUEST_KEY_DATA_WAS_CHANGED -> {
                val needSaveData: Boolean = result.getBoolean(ANSWER_ARG_NAME_DATA_WAS_CHANGED, false)
                if (needSaveData) {
                    viewModel.handleEvent(WorkOrderDetailEvent.OnSave(shouldCloseScreen = true))
                } else {
                    sendResultToFragment()
                    findNavController().popBackStack()
                }
            }

            // пользователь удалил строку в ТЧ "Работы":
            REQUEST_DELETE_JOB_DETAIL_KEY -> {
                val delete: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (delete) {
                    viewModel.handleEvent(WorkOrderDetailEvent.OnJobDetailDelete)
                    jobDetailListAdapter.notifyDataSetChanged()
                }
            }

            // пользователь удалил строку в ТЧ "Исполнители":
            REQUEST_DELETE_PERFORMER_DETAIL_KEY -> {
                val delete: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (delete) {
                    viewModel.handleEvent(WorkOrderDetailEvent.OnPerformerDetailDelete)
                    performerDetailListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.handleEvent(WorkOrderDetailEvent.OnSave(shouldCloseScreen = false))
        }

        binding.closeButton.setOnClickListener {
            onCloseWorkOrder()
        }

        binding.dateSelectButton.setOnClickListener {
            DatePickerFragment
                .newInstance(viewModel.screenState.value.workOrder.date, REQUEST_DATE_PICKER_KEY, ARG_DATE)
                .show(childFragmentManager, REQUEST_DATE_PICKER_KEY)
        }

        binding.partnerSelectButton.setOnClickListener {
            viewModel.screenState.value.workOrder.partner?.isSelected = true

            PartnerPickerFragment
                .newInstance(viewModel.screenState.value.workOrder.partner, REQUEST_PARTNER_PICKER_KEY, ARG_PARTNER)
                .show(childFragmentManager, REQUEST_PARTNER_PICKER_KEY)
        }

        binding.carSelectButton.setOnClickListener {
            viewModel.screenState.value.workOrder.partner?.let { partner ->
                viewModel.screenState.value.workOrder.car?.isSelected = true

                CarPickerFragment
                    .newInstance(viewModel.screenState.value.workOrder.car, partner, REQUEST_CAR_PICKER_KEY, ARG_CAR)
                    .show(childFragmentManager, REQUEST_CAR_PICKER_KEY)
            } ?: let {
                MessageDialogFragment.newInstance(getString(R.string.error_specify_partner))
                    .show(childFragmentManager, null)
            }
        }

        binding.repairTypeSelectButton.setOnClickListener {
            viewModel.screenState.value.workOrder.repairType?.isSelected = true

            RepairTypePickerFragment
                .newInstance(
                    viewModel.screenState.value.workOrder.repairType,
                    REQUEST_REPAIR_TYPE_PICKER_KEY,
                    ARG_REPAIR_TYPE
                )
                .show(childFragmentManager, REQUEST_REPAIR_TYPE_PICKER_KEY)
        }

        binding.masterSelectButton.setOnClickListener {
            viewModel.screenState.value.workOrder.master?.isSelected = true

            EmployeePickerFragment
                .newInstance(viewModel.screenState.value.workOrder.master, REQUEST_MASTER_PICKER_KEY, ARG_MASTER)
                .show(childFragmentManager, REQUEST_MASTER_PICKER_KEY)
        }

        binding.departmentSelectButton.setOnClickListener {
            viewModel.screenState.value.workOrder.department?.isSelected = true

            DepartmentPickerFragment
                .newInstance(
                    viewModel.screenState.value.workOrder.department,
                    REQUEST_DEPARTMENT_PICKER_KEY,
                    ARG_DEPARTMENT
                )
                .show(childFragmentManager, REQUEST_DEPARTMENT_PICKER_KEY)
        }

        binding.addJobDetailButton.setOnClickListener {
            JobDetailFragment
                .newInstance(
                    JobDetail.getNewJobDetail(viewModel.screenState.value.workOrder),
                    REQUEST_JOB_DETAIL_PICKER_KEY,
                    ARG_JOB_DETAIL
                ) // здесь надо подумать как правильно создавать новую строку ТЧ
                .show(childFragmentManager, REQUEST_JOB_DETAIL_PICKER_KEY)
        }

        binding.editJobDetailButton.setOnClickListener {
            viewModel.screenState.value.selectedJobDetail?.let {
                JobDetailFragment
                    .newInstance(it, REQUEST_JOB_DETAIL_PICKER_KEY, ARG_JOB_DETAIL)
                    .show(childFragmentManager, REQUEST_JOB_DETAIL_PICKER_KEY)
            } ?: let {
                MessageDialogFragment.newInstance(getString(R.string.edit_job_detail_not_selected))
                    .show(childFragmentManager, null)
            }
        }

        binding.deleteJobDetailButton.setOnClickListener {
            viewModel.screenState.value.selectedJobDetail?.let {
                QuestionDialogFragment
                    .newInstance(
                        getString(R.string.delete_job_detail_question),
                        REQUEST_DELETE_JOB_DETAIL_KEY,
                        ARG_ANSWER
                    )
                    .show(childFragmentManager, REQUEST_DELETE_JOB_DETAIL_KEY)
            } ?: let {
                MessageDialogFragment.newInstance(getString(R.string.delete_job_detail_not_selected))
                    .show(childFragmentManager, null)
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
            PerformerDetailFragment
                .newInstance(
                    PerformerDetail.getNewPerformerDetail(viewModel.screenState.value.workOrder),
                    REQUEST_PERFORMER_DETAIL_PICKER_KEY,
                    ARG_PERFORMER_DETAIL
                ) // здесь надо подумать как правильно создавать новую строку ТЧ
                .show(childFragmentManager, REQUEST_PERFORMER_DETAIL_PICKER_KEY)
        }

        binding.editPerformerDetailButton.setOnClickListener {
            viewModel.screenState.value.selectedPerformerDetail?.let {
                PerformerDetailFragment
                    .newInstance(it, REQUEST_PERFORMER_DETAIL_PICKER_KEY, ARG_PERFORMER_DETAIL)
                    .show(childFragmentManager, REQUEST_PERFORMER_DETAIL_PICKER_KEY)
            } ?: let {
                MessageDialogFragment.newInstance(getString(R.string.edit_performer_detail_not_selected))
                    .show(childFragmentManager, null)
            }
        }

        binding.deletePerformerDetailButton.setOnClickListener {
            viewModel.screenState.value.selectedPerformerDetail?.let {
                QuestionDialogFragment
                    .newInstance(
                        getString(R.string.delete_performer_detail_question),
                        REQUEST_DELETE_PERFORMER_DETAIL_KEY,
                        ARG_ANSWER
                    )
                    .show(childFragmentManager, REQUEST_DELETE_PERFORMER_DETAIL_KEY)
            } ?: let {
                MessageDialogFragment.newInstance(getString(R.string.delete_performer_detail_not_selected))
                    .show(childFragmentManager, null)
            }
        }

        binding.sendToEmailButton.setOnClickListener {
            if (viewModel.screenState.value.isModified) {
                MessageDialogFragment.newInstance(getString(R.string.save_work_order_before))
                    .show(childFragmentManager, null)
            } else {
                if (viewModel.screenState.value.workOrder.isNew) {
                    MessageDialogFragment.newInstance(getString(R.string.can_not_send_work_order))
                        .show(childFragmentManager, null)
                } else {
                    if (binding.emailEditText.text.toString().isEmpty()) {
                        viewModel.handleEvent(WorkOrderDetailEvent.OnIncorrectEmail)
                        MessageDialogFragment.newInstance(getString(R.string.fill_in_email))
                            .show(childFragmentManager, null)
                    } else {
                        val isEmailValid =
                            Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()
                        if (isEmailValid) {
                            // показать фрагмент
                            val emailAddress: String = viewModel.parseText(binding.emailEditText.text.toString())

                            SendWorkOrderByIdToEmailDialogFragment
                                .newInstance(viewModel.screenState.value.workOrder.id, emailAddress)
                                .show(childFragmentManager, null)
                        } else {
                            viewModel.handleEvent(WorkOrderDetailEvent.OnIncorrectEmail)
                            MessageDialogFragment.newInstance(getString(R.string.wrong_email))
                                .show(childFragmentManager, null)
                        }
                    }
                }
            }
        }
    }

    private fun getSumFromJobDetail(jobDetails: List<JobDetail>): BigDecimal {
        var sum = BigDecimal.ZERO
        for (jobDet in jobDetails) {
            sum = sum + jobDet.sum
        }
        return sum
    }

    private fun refreshTotalSum() {
        // показать итоговую сумму:
        binding.totalSumTextView.setText(
            getSumFromJobDetail(viewModel.screenState.value.workOrder.jobDetails).toString()
        )
    }

    private fun sendResultToFragment() {
        // отправка информации в фрагмент: WorkOrderListFragment
        val bundle = Bundle().apply {
            putParcelable(argNameReturnResult, viewModel.returnResult)
        }
        setFragmentResult(requestKeyReturnResult, bundle)
    }

    companion object {
        // эти константы нужны для диалогового окна - "Данные изменились. Записать?"
        private val REQUEST_KEY_DATA_WAS_CHANGED = "request_key_data_was_changed"
        private val ANSWER_ARG_NAME_DATA_WAS_CHANGED = "answer_arg_name_data_was_changed"

        private val REQUEST_DATE_PICKER_KEY = "request_date_picker_key"
        private val ARG_DATE = "date_picker"

        private val REQUEST_PARTNER_PICKER_KEY = "request_partner_picker_key"
        private val ARG_PARTNER = "partner_picker"

        private val REQUEST_CAR_PICKER_KEY = "request_car_picker_key"
        private val ARG_CAR = "car_picker"

        private val REQUEST_REPAIR_TYPE_PICKER_KEY = "request_repair_type_picker_key"
        private val ARG_REPAIR_TYPE = "repair_type_picker"

        private val REQUEST_MASTER_PICKER_KEY = "request_master_picker_key"
        private val ARG_MASTER = "master_picker"

        private val REQUEST_DEPARTMENT_PICKER_KEY = "request_department_picker_key"
        private val ARG_DEPARTMENT = "department_picker"

        private val REQUEST_JOB_DETAIL_PICKER_KEY = "request_job_detail_picker_key"
        private val REQUEST_PERFORMER_DETAIL_PICKER_KEY = "request_performer_detail_picker_key"
        private val ARG_JOB_DETAIL = "job_detail_picker"
        private val ARG_PERFORMER_DETAIL = "performer_detail_picker"

        private val REQUEST_DATA_WAS_CHANGED_KEY = "data_was_changed_key"
        private val REQUEST_DELETE_JOB_DETAIL_KEY = "delete_job_detail_key"
        private val REQUEST_DELETE_PERFORMER_DETAIL_KEY = "delete_performer_detail_key"
        private val REQUEST_ADD_DEFAULT_JOBS_KEY = "add_default_jobs_key"
        private val ARG_ANSWER = "answer"
    }
}
