package ru.internetcloud.workorderapplication.presentation.workorder.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetDefaultRepairTypeJobsUseCaseQualifier
import ru.internetcloud.workorderapplication.domain.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.document.JobDetail
import ru.internetcloud.workorderapplication.domain.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetDefaultRepairTypeJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.UpdateWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.settingsoperation.GetDefaultWorkOrderSettingsUseCase
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class WorkOrderViewModel @Inject constructor(
    private val getWorkOrderUseCase: GetWorkOrderUseCase,
    private val updateWorkOrderUseCase: UpdateWorkOrderUseCase,
    private val getDefaultWorkOrderSettingsUseCase: GetDefaultWorkOrderSettingsUseCase,

    @DbGetDefaultRepairTypeJobsUseCaseQualifier
    private val getDefaultRepairTypeJobsUseCase: GetDefaultRepairTypeJobsUseCase
) : ViewModel() {

    // LiveData-объекты, с помощью которых будет отображение данных в элементах управления:
    private val _workOrder = MutableLiveData<WorkOrder>()
    val workOrder: LiveData<WorkOrder>
        get() = _workOrder

    // можно ли завершить (или закрыть)
    private val _canFinish = MutableLiveData<Boolean>()
    val canFinish: LiveData<Boolean>
        get() = _canFinish

    // для обработки ошибок:
    private val _errorInputNumber = MutableLiveData<Boolean>()
    val errorInputNumber: LiveData<Boolean>
        get() = _errorInputNumber

    private val _errorInputEmail = MutableLiveData<Boolean>()
    val errorInputEmail: LiveData<Boolean>
        get() = _errorInputEmail

    private val _canFillDefaultJobs = MutableLiveData<Boolean>()
    val canFillDefaultJobs: LiveData<Boolean>
        get() = _canFillDefaultJobs


    var errorInputPerformer: Boolean = false

    private val _showErrorMessage = MutableLiveData<Boolean>()
    val showErrorMessage: LiveData<Boolean>
        get() = _showErrorMessage

    var closeOnSave: Boolean = false
    var selectedJobDetail: JobDetail? = null
    var selectedPerformerDetail: PerformerDetail? = null

    var isChanged: Boolean = false

    var defaultCarJobs: List<DefaultRepairTypeJobDetail> = mutableListOf()
    var defaultWorkOrderSettings: DefaultWorkOrderSettings? = null

    init {
        // Log.i("rustam", "сработал блок init в WorkOrderViewModel")
    }

    companion object {
        private const val NUMBER_PREFIX = "new"
        private const val SPACE_SYMBOL = " "
        private const val NOT_FOUND = -1
    }

    // -------------------------------------------------------------------------------
    fun loadWorkOrder(workOrderId: String) {
        viewModelScope.launch {
            defaultWorkOrderSettings = getDefaultWorkOrderSettingsUseCase.getDefaultWorkOrderSettings()
            val order = getWorkOrderUseCase.getWorkOrder(workOrderId)
            order?.let {
                _workOrder.value = it
            } ?: run {
                createWorkOrder()
            }
        }
    }

    fun updateWorkOrder() {
        val areFieldsValid = validateInput()
        if (areFieldsValid) {
            _workOrder.value?.let { order ->
                viewModelScope.launch {
                    updatePerformersString(order)
                    updateWorkOrderUseCase.updateWorkOrder(order)
                    _canFinish.value = true
                }
            }
        } else {
            _showErrorMessage.value = true
        }
    }

    private fun validateInput(): Boolean {
        var result = true
        workOrder.value?.let { order ->
            if (order.number.isBlank()) {
                _errorInputNumber.value = true
                closeOnSave = false
                result = false
            }

            if (order.performers.isEmpty()) {
                errorInputPerformer = true
                closeOnSave = false
                result = false
            }
        }
        return result
    }

    fun resetErrorInputNumber() {
        _errorInputNumber.value = false
    }

    fun resetErrorInputEmail() {
        _errorInputEmail.value = false
    }

    fun resetShowErrorMessage() {
        _showErrorMessage.value = false
    }

    fun createWorkOrder() {
        viewModelScope.launch {
            val newWorkOrder = WorkOrder(
                id = NUMBER_PREFIX + UUID.randomUUID().toString(),
                isNew = true
            )

            defaultWorkOrderSettings = getDefaultWorkOrderSettingsUseCase.getDefaultWorkOrderSettings()
            defaultWorkOrderSettings?.let { settings ->
                newWorkOrder.department = settings.department
                newWorkOrder.master = settings.master
                settings.employee?.let { newEmployee ->
                    val newPerformerDetail = PerformerDetail.getNewPerformerDetail(newWorkOrder)
                    newPerformerDetail.employee = newEmployee
                    newWorkOrder.performers.add(newPerformerDetail)
                }
            }
            _workOrder.value = newWorkOrder
        }
    }

    fun getPerformersString(order: WorkOrder): String {
        var performersString = ""
        for (performer in order.performers) {
            performer.employee?.let {
                performersString = performersString + "; " + getShortName(it.name)
            }
        }
        val performerLength = performersString.length
        if (performerLength > 2) {
            performersString = performersString.substring(2)
        }
        return performersString
    }

    fun getShortName(name: String): String {
        var shortName = ""

        val firstSpacePos = name.indexOf(SPACE_SYMBOL)
        val secondSpacePos = name.indexOf(SPACE_SYMBOL, firstSpacePos + 1)

        if (secondSpacePos == NOT_FOUND) {
            shortName = name
        } else {
            shortName = name.substring(0, secondSpacePos)
        }

        return shortName
    }

    fun updatePerformersString(order: WorkOrder) {
        order.performersString = getPerformersString(order)
    }

    fun setErrorEmailValue(value: Boolean) {
        _errorInputEmail.value = value
    }

    fun fillDefaultJobs() {
        if (!defaultCarJobs.isEmpty()) {
            workOrder.value?.let { order ->

                defaultWorkOrderSettings?.let { settings ->

                    settings.workingHour?.let { wh ->

                        order.jobDetails.forEach {
                            it.isSelected = false
                        }

                        val carModel = order.car?.carModel
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
                                        val newJobDetail = JobDetail.getNewJobDetail(order)
                                        newJobDetail.carJob = currentCarJob.carJob
                                        newJobDetail.quantity = currentCarJob.quantity
                                        newJobDetail.timeNorm = settings.defaultTimeNorm
                                        newJobDetail.workingHour = wh
                                        newJobDetail.sum =
                                            newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                        order.jobDetails.add(newJobDetail)
                                    }
                                }
                            } else {
                                for (currentCarJob in defaultCarJobs) {
                                    if (currentCarJob.carModel == null) {
                                        val newJobDetail = JobDetail.getNewJobDetail(order)
                                        newJobDetail.carJob = currentCarJob.carJob
                                        newJobDetail.quantity = currentCarJob.quantity
                                        newJobDetail.timeNorm = settings.defaultTimeNorm
                                        newJobDetail.workingHour = wh
                                        newJobDetail.sum =
                                            newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                        order.jobDetails.add(newJobDetail)
                                    }
                                }
                            }
                        } ?: let {
                            // случай, когда модель не заполнена, заполняем все работы с незаполненной моделью:
                            for (currentCarJob in defaultCarJobs) {
                                if (currentCarJob.carModel == null) {
                                    val newJobDetail = JobDetail.getNewJobDetail(order)
                                    newJobDetail.carJob = currentCarJob.carJob
                                    newJobDetail.quantity = currentCarJob.quantity
                                    newJobDetail.timeNorm = settings.defaultTimeNorm
                                    newJobDetail.workingHour = wh
                                    newJobDetail.sum =
                                        newJobDetail.quantity * newJobDetail.timeNorm * wh.price
                                    order.jobDetails.add(newJobDetail)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun checkDefaultRepairTypeJobDetails(repairType: RepairType) {
        defaultCarJobs = mutableListOf()
        _canFillDefaultJobs.value = false

        viewModelScope.launch {
            if (repairType != null && workOrder.value?.car != null) {
                defaultCarJobs = getDefaultRepairTypeJobsUseCase.getDefaultRepairTypeJobDetails(repairType)
                if (!defaultCarJobs.isEmpty()) {

                    _canFillDefaultJobs.value = true

                }
            }
        }
    }
}
