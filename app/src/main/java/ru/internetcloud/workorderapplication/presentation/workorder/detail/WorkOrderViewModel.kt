package ru.internetcloud.workorderapplication.presentation.workorder.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.model.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.model.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.model.document.JobDetail
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetDefaultRepairTypeJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.UpdateWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.settingsoperation.GetDefaultWorkOrderSettingsUseCase
import ru.internetcloud.workorderapplication.presentation.util.ReturnResult

@HiltViewModel
class WorkOrderViewModel @Inject constructor(
    private val getWorkOrderUseCase: GetWorkOrderUseCase,
    private val updateWorkOrderUseCase: UpdateWorkOrderUseCase,
    private val getDefaultWorkOrderSettingsUseCase: GetDefaultWorkOrderSettingsUseCase,
    private val getDefaultRepairTypeJobsUseCase: GetDefaultRepairTypeJobsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: WorkOrderFragmentArgs = WorkOrderFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val workOrderId: String = navArgs.workOrderId
    private val editMode: EditMode = navArgs.editMode

    // если ОС Андроид "убъет" приложение - чтобы как Феникс из пепла восстановилось со всем состоянием!
    val screenState: StateFlow<UiWorkOrderDetailState> = savedStateHandle.getStateFlow(
        KEY_WORK_ORDER_DETAIL_STATE,
        UiWorkOrderDetailState()
    )

    private val screenEventChannel = Channel<WorkOrderDetailScreenEvent>(Channel.BUFFERED)
    val screenEventFlow = screenEventChannel.receiveAsFlow()

    var returnResult: ReturnResult =
        ReturnResult.NoOperation // почему бы returnResult в screenState не добавить? Подумай...

    var defaultWorkOrderSettings: DefaultWorkOrderSettings? = null
    var defaultCarJobs: List<DefaultRepairTypeJobDetail> = emptyList() // mutableListOf()

    //var selectedPerformerDetail: PerformerDetail? = null
    var selectedJobDetail: JobDetail? = null

    init {
        if (screenState.value.shouldInit) {
            when (editMode) {
                EditMode.Add -> createWorkOrder()
                EditMode.Edit -> fetchWorkOrder(workOrderId = workOrderId)
            }
        }
    }

    private fun createWorkOrder() {
        viewModelScope.launch {
            var newWorkOrder = WorkOrder(
                id = NUMBER_PREFIX + UUID.randomUUID().toString(),
                isNew = true
            )

            defaultWorkOrderSettings = getDefaultWorkOrderSettingsUseCase.getDefaultWorkOrderSettings()
            defaultWorkOrderSettings?.let { settings ->

                newWorkOrder = newWorkOrder.copy(
                    department = settings.department,
                    master = settings.master
                )

                settings.employee?.let { newEmployee ->
                    val newPerformerDetail = PerformerDetail.getNewPerformerDetail(newWorkOrder)
                    newPerformerDetail.employee = newEmployee
                    val mutableList = mutableListOf<PerformerDetail>()
                    mutableList.add(newPerformerDetail)

                    newWorkOrder = newWorkOrder.copy(
                        performers = mutableList.toList()
                    )
                }
            }
            savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                shouldInit = false,
                loading = false,
                workOrder = newWorkOrder
            )
        }
    }

    private fun fetchWorkOrder(workOrderId: String) {
        viewModelScope.launch {
            savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                loading = true
            )

            defaultWorkOrderSettings = getDefaultWorkOrderSettingsUseCase.getDefaultWorkOrderSettings()

            try {
                val workOrder = getWorkOrderUseCase.getWorkOrder(workOrderId = workOrderId)
                workOrder?.let { currentWorkOrder ->
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        shouldInit = false,
                        loading = false,
                        workOrder = currentWorkOrder
                    )
                } ?: error("Not found WorkOrder with id = $workOrderId")
            } catch (e: Exception) {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    loading = false,
                    error = e
                )
            }
        }
    }

    fun handleEvent(event: WorkOrderDetailEvent) {
        val oldWorkOrder = screenState.value.workOrder

        when (event) {
            is WorkOrderDetailEvent.OnNumberChange -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(number = event.number),
                    isModified = true,
                    errorInputNumber = false
                )
            }

            is WorkOrderDetailEvent.OnEmailChange -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    errorInputEmail = false
                )
            }

            is WorkOrderDetailEvent.OnMileageChange -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(mileage = event.mileage),
                    isModified = true
                )
            }

            is WorkOrderDetailEvent.OnRequestReasonChange -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(requestReason = event.requestReason),
                    isModified = true
                )
            }

            is WorkOrderDetailEvent.OnCommentChange -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(comment = event.comment),
                    isModified = true
                )
            }

            is WorkOrderDetailEvent.OnDateChange -> {
                if (oldWorkOrder.date != event.date) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(date = event.date),
                        isModified = true
                    )
                }
            }

            is WorkOrderDetailEvent.OnSave -> {
                saveWorkOrder(shouldCloseScreen = false)
            }

            is WorkOrderDetailEvent.OnPartnerChange -> {
                if (oldWorkOrder.partner != event.partner) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(
                            partner = event.partner,
                            car = null // т.к. изменили Заказчика надо очистить СХТ, .т.к. СХТ от другого заказчика
                        ),
                        isModified = true
                    )
                }
            }

            is WorkOrderDetailEvent.OnCarChange -> {
                if (oldWorkOrder.car != event.car) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(car = event.car),
                        isModified = true
                    )
                }
            }

            is WorkOrderDetailEvent.OnRepairTypeChange -> {
                if (oldWorkOrder.repairType != event.repairType) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(repairType = event.repairType),
                        isModified = true
                    )
                    // после выбора Вида ремонта:
                    // у Вида ремонта могут быть Работы по-умолчанию, надо их найти и предложить заполнить:
                    checkDefaultRepairTypeJobDetails(event.repairType)
                }
            }

            is WorkOrderDetailEvent.OnDepartmentChange -> {
                if (oldWorkOrder.department != event.department) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(department = event.department),
                        isModified = true
                    )
                }
            }

            is WorkOrderDetailEvent.OnMasterChange -> {
                if (oldWorkOrder.master != event.master) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(master = event.master),
                        isModified = true
                    )
                }
            }

            WorkOrderDetailEvent.OnIncorrectEmail -> {
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    errorInputEmail = true
                )
            }

            is WorkOrderDetailEvent.OnJobDetailChange -> {
                // ввели строку ТЧ "Работы":
                event.jobDetail?.let { jobdet ->
                    val mutableJobDetailsList = oldWorkOrder.jobDetails.toMutableList()

                    val foundJobDetail = mutableJobDetailsList.find { it.lineNumber == jobdet.lineNumber }
                    foundJobDetail?.copyFields(jobdet)
                        ?: let {
                            // это новая строка, добавленная в ТЧ
                            oldWorkOrder.jobDetails.forEach {
                                it.isSelected = false
                            }

                            //oldWorkOrder.jobDetails.add(jobdet)
                            jobdet.isSelected = true
                            selectedJobDetail = jobdet

                            mutableJobDetailsList.add(jobdet)
                            // binding.jobDetailsRecyclerView.scrollToPosition(order.jobDetails.indexOf(jobdet))
                        }
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(jobDetails = mutableJobDetailsList.toList()),
                        isModified = true
                    )
//                        jobDetailListAdapter.notifyItemChanged(
//                            order.jobDetails.indexOf(viewModel.selectedJobDetail),
//                            Unit
//                        )
//                        refreshTotalSum()
                }
            }

            is WorkOrderDetailEvent.OnPerformerDetailChange -> {
                // ввели строку ТЧ "Исполнители":
                event.performerDetail?.let { currentPerformerDetail ->
                    val mutablePerformerDetailsList = oldWorkOrder.performers.toMutableList()

                    val foundPerformerDetail =
                        mutablePerformerDetailsList.find { it.lineNumber == currentPerformerDetail.lineNumber }
                    foundPerformerDetail?.copyFields(currentPerformerDetail)
                        ?: let {
                            // это новая строка, добавленная в ТЧ
                            mutablePerformerDetailsList.forEach {
                                it.isSelected = false
                            }
                            currentPerformerDetail.isSelected = true
                            mutablePerformerDetailsList.add(currentPerformerDetail)
                        }
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = oldWorkOrder.copy(performers = mutablePerformerDetailsList.toList()),
                        selectedPerformerDetail = currentPerformerDetail,
                        isModified = true
                    )
                }
            }

            WorkOrderDetailEvent.OnJobDetailDelete -> {
                // пользователь удалил строку в ТЧ "Работы":
                val mutableJobDetailsList = oldWorkOrder.jobDetails.toMutableList()
                mutableJobDetailsList.remove(selectedJobDetail)
                selectedJobDetail = null

                var pos = 0
                mutableJobDetailsList.forEach {
                    pos++
                    it.lineNumber = pos
                }
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(jobDetails = mutableJobDetailsList.toList()),
                    isModified = true
                )
//                jobDetailListAdapter.notifyDataSetChanged()
//                refreshTotalSum()
            }

            WorkOrderDetailEvent.OnPerformerDetailDelete -> {
                // пользователь удалил строку в ТЧ "Исполнители":
                val mutablePerformerDetailsList = oldWorkOrder.performers.toMutableList()
                mutablePerformerDetailsList.remove(screenState.value.selectedPerformerDetail)

                var pos = 0
                mutablePerformerDetailsList.forEach {
                    pos++
                    it.lineNumber = pos
                }
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    workOrder = oldWorkOrder.copy(performers = mutablePerformerDetailsList.toList()),
                    selectedPerformerDetail = null,
                    isModified = true
                )
            }

            is WorkOrderDetailEvent.OnSelectedPerformerChange -> {
                screenState.value.selectedPerformerDetail?.isSelected = false
                event.performerDetail.isSelected = true
                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                    selectedPerformerDetail = event.performerDetail
                )
            }
        }
    }

    fun saveWorkOrder(shouldCloseScreen: Boolean = false) {
        viewModelScope.launch {
            // перед началом сохранения можно показать прогресс-бар и заблокировать кнопку "SAVE" и остальные элементы заблокировать
            savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                saving = true
            )
            val oldWorkOrder = screenState.value.workOrder
            val parsedWorkOrder = oldWorkOrder.copy(
                number = parseText(oldWorkOrder.number),
                requestReason = parseText(oldWorkOrder.requestReason),
                comment = parseText(oldWorkOrder.comment),
                isModified = oldWorkOrder.isModified || screenState.value.isModified
            )

            val valid = validateInput(parsedWorkOrder)

            if (valid) {
                try {
                    when (editMode) {
                        EditMode.Add, EditMode.Edit -> {
                            updateWorkOrderUseCase.updateWorkOrder(workOrder = parsedWorkOrder)
                        }
                    }

                    returnResult = ReturnResult.Changed(workOrderId = parsedWorkOrder.id)

                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = parsedWorkOrder,
                        saving = false,
                        isModified = false,
                        shouldCloseScreen = shouldCloseScreen
                    )

                    if (!shouldCloseScreen) {
                        screenEventChannel.trySend(WorkOrderDetailScreenEvent.ShowSavingSuccess)
                    }
                } catch (e: Exception) {
                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                        workOrder = parsedWorkOrder,
                        loading = false,
                        saving = false,
                        error = e
                    )
                }
            }
        }
    }

    fun parseText(input: String?): String {
        return input?.trim() ?: DEFAULT_STRING_VALUE
    }

    private fun validateInput(workOrder: WorkOrder): Boolean {
        val valid = workOrder.number.isNotBlank() && workOrder.performers.isNotEmpty()
        // ToDo надо еще проверить а не существует ли в Room заказ-наряд с таким номером?

        if (!valid) {
            savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                saving = false,
                errorInputNumber = workOrder.number.isBlank(),
                errorInputPerformer = workOrder.performers.isEmpty()
            )

            screenEventChannel
                .trySend(
                    WorkOrderDetailScreenEvent.ShowFieldsError(
                        errorInputNumber = workOrder.number.isBlank(),
                        errorInputPerformer = workOrder.performers.isEmpty()
                    )
                )
        }
        return valid
    }

    private fun checkDefaultRepairTypeJobDetails(repairType: RepairType?) {
        defaultCarJobs = emptyList() //mutableListOf()
        savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
            canFillDefaultJobs = false
        )

        viewModelScope.launch {
            if (repairType != null && screenState.value.workOrder.car != null) {
                defaultCarJobs = getDefaultRepairTypeJobsUseCase.getDefaultRepairTypeJobDetails(repairType)
                if (defaultCarJobs.isNotEmpty()) {
                    screenState.value.workOrder.car?.let { car ->
                        for (currentCarJob in defaultCarJobs) {
                            if (currentCarJob.carModel == null || currentCarJob.carModel == car.carModel) {
                                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                                    canFillDefaultJobs = true
                                )
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    fun fillDefaultJobs() {
        if (defaultCarJobs.isNotEmpty()) {

            defaultWorkOrderSettings?.let { settings ->

                settings.workingHour?.let { wh ->

                    screenState.value.workOrder.jobDetails.forEach {
                        it.isSelected = false
                    }

                    val carModel = screenState.value.workOrder.car?.carModel
                    carModel?.let { model ->
                        // когда модель заполнена: 2 случая
                        var isModelExist = false
                        for (currentCarJob in defaultCarJobs) {
                            if (currentCarJob.carModel == model) {
                                isModelExist = true
                                break
                            }
                        }

                        if (isModelExist) {
                            for (currentCarJob in defaultCarJobs) {
                                if (currentCarJob.carModel == model) {
                                    val newJobDetail = JobDetail.getNewJobDetail(screenState.value.workOrder)
                                    newJobDetail.carJob = currentCarJob.carJob
                                    newJobDetail.quantity = currentCarJob.quantity
                                    newJobDetail.timeNorm = settings.defaultTimeNorm
                                    newJobDetail.workingHour = wh
                                    newJobDetail.sum = newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                    //screenState.value.workOrder.jobDetails.add(newJobDetail)

                                    val mutableList = screenState.value.workOrder.jobDetails.toMutableList()
                                    mutableList.add(newJobDetail)

                                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                                        workOrder = screenState.value.workOrder.copy(
                                            jobDetails = mutableList.toList()
                                        )
                                    )
                                }
                            }
                        } else {
                            for (currentCarJob in defaultCarJobs) {
                                if (currentCarJob.carModel == null) {
                                    val newJobDetail = JobDetail.getNewJobDetail(screenState.value.workOrder)
                                    newJobDetail.carJob = currentCarJob.carJob
                                    newJobDetail.quantity = currentCarJob.quantity
                                    newJobDetail.timeNorm = settings.defaultTimeNorm
                                    newJobDetail.workingHour = wh
                                    newJobDetail.sum = newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                    //order.jobDetails.add(newJobDetail)
                                    val mutableList = screenState.value.workOrder.jobDetails.toMutableList()
                                    mutableList.add(newJobDetail)

                                    savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                                        workOrder = screenState.value.workOrder.copy(
                                            jobDetails = mutableList.toList()
                                        )
                                    )
                                }
                            }
                        }
                    } ?: let {
                        // случай, когда модель не заполнена, заполняем все работы с незаполненной моделью:
                        for (currentCarJob in defaultCarJobs) {
                            if (currentCarJob.carModel == null) {
                                val newJobDetail = JobDetail.getNewJobDetail(screenState.value.workOrder)
                                newJobDetail.carJob = currentCarJob.carJob
                                newJobDetail.quantity = currentCarJob.quantity
                                newJobDetail.timeNorm = settings.defaultTimeNorm
                                newJobDetail.workingHour = wh
                                newJobDetail.sum = newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                //order.jobDetails.add(newJobDetail)
                                val mutableList = screenState.value.workOrder.jobDetails.toMutableList()
                                mutableList.add(newJobDetail)

                                savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
                                    workOrder = screenState.value.workOrder.copy(
                                        jobDetails = mutableList.toList()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

//    fun changeSelectedPerformer(performerDetail: PerformerDetail) {
//        selectedPerformerDetail?.isSelected = false
//        performerDetail.isSelected = true
//        selectedPerformerDetail = performerDetail
//    }

    fun changeSelectedJob(jobDetail: JobDetail) {
        selectedJobDetail?.isSelected = false
        selectedJobDetail = jobDetail
        selectedJobDetail?.isSelected = true
    }

    fun resetcanFillDefaultJobs() {
        savedStateHandle[KEY_WORK_ORDER_DETAIL_STATE] = screenState.value.copy(
            canFillDefaultJobs = false
        )
    }

//    fun updateWorkOrder() {
//        val areFieldsValid = validateInput()
//        if (areFieldsValid) {
//            _workOrder.value?.let { order ->
//                viewModelScope.launch {
//                    updatePerformersString(order)
//                    // ToDo подумать над тем, если пользователь успевает нажать BACK и ничего не успеет сохраниться?
//                    updateWorkOrderUseCase.updateWorkOrder(order)
//                    _canFinish.value = true
//                }
//            }
//        } else {
//            _showErrorMessage.value = true
//        }
//    }
//
//    fun getPerformersString(order: WorkOrder): String {
//        var performersString = ""
//        for (performer in order.performers) {
//            performer.employee?.let {
//                performersString = performersString + "; " + getShortName(it.name)
//            }
//        }
//        val performerLength = performersString.length
//        if (performerLength > 2) {
//            performersString = performersString.substring(2)
//        }
//        return performersString
//    }
//
//    fun getShortName(name: String): String {
//        var shortName = ""
//
//        val firstSpacePos = name.indexOf(SPACE_SYMBOL)
//        val secondSpacePos = name.indexOf(SPACE_SYMBOL, firstSpacePos + 1)
//
//        if (secondSpacePos == NOT_FOUND) {
//            shortName = name
//        } else {
//            shortName = name.substring(0, secondSpacePos)
//        }
//
//        return shortName
//    }
//
//    fun updatePerformersString(order: WorkOrder) {
//        order.performersString = getPerformersString(order)
//    }
//
//    fun setErrorEmailValue(value: Boolean) {
//        _errorInputEmail.value = value
//    }
//
//
//    fun checkDefaultRepairTypeJobDetails(repairType: RepairType) {
//        defaultCarJobs = mutableListOf()
//        _canFillDefaultJobs.value = false
//
//        viewModelScope.launch {
//            if (repairType != null && workOrder.value?.car != null) {
//                defaultCarJobs = getDefaultRepairTypeJobsUseCase.getDefaultRepairTypeJobDetails(repairType)
//                if (!defaultCarJobs.isEmpty()) {
//                    workOrder.value?.car?.let { car ->
//                        for (currentCarJob in defaultCarJobs) {
//                            if (currentCarJob.carModel == null || currentCarJob.carModel == car.carModel) {
//                                _canFillDefaultJobs.value = true
//                                break
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


    companion object {
        private const val DEFAULT_STRING_VALUE = ""
        private const val NUMBER_PREFIX = "new"
        private const val SPACE_SYMBOL = " "
        private const val NOT_FOUND = -1
        private const val KEY_WORK_ORDER_DETAIL_STATE = "key_work_order_detail_state"
    }
}
