package ru.internetcloud.workorderapplication.presentation.workorder.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.db.DbPartnerRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.AddWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.UpdateWorkOrderUseCase
import java.util.*

class WorkOrderViewModel : ViewModel() {

    // private val repository = LocalWorkOrderRepositoryImpl // требуется инъекция зависимостей!!!
    private val workOrderRepository = DbWorkOrderRepositoryImpl.get()
    private val partnerRepository = DbPartnerRepositoryImpl.get()

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getWorkOrderUseCase = GetWorkOrderUseCase(workOrderRepository)
    private val addWorkOrderUseCase = AddWorkOrderUseCase(workOrderRepository)
    private val updateWorkOrderUseCase = UpdateWorkOrderUseCase(workOrderRepository)

    private val getPartnerUseCase = GetPartnerUseCase(partnerRepository)

    // LiveData-объекты, с помощью которых будет отображение данных в элементах управления:
    private val _workOrder = MutableLiveData<WorkOrder>()
    val workOrder: LiveData<WorkOrder>
        get() = _workOrder

    private val _repairTypes = MutableLiveData<List<RepairType>>()
    val repairTypes: LiveData<List<RepairType>>
        get() = _repairTypes

    // можно ли завершить (или закрыть)
    private val _canFinish = MutableLiveData<Boolean>()
    val canFinish: LiveData<Boolean>
        get() = _canFinish

    // для обработки ошибок:
    private val _errorInputNumber = MutableLiveData<Boolean>()
    val errorInputNumber: LiveData<Boolean>
        get() = _errorInputNumber

    companion object {
        private const val NUMBER_PREFIX = "new"
    }


    // -------------------------------------------------------------------------------
    fun loadWorkOrder(workOrderId: String) {
        viewModelScope.launch {
            val order = getWorkOrderUseCase.getWorkOrder(workOrderId)
            order?.let {
                _workOrder.value = it
            } ?: run {
                createWorkOrder()
            }
        }
    }

    fun addWorkOrder() {
        val areFieldsValid = validateInput()
        if (areFieldsValid) {
            workOrder.value?.let { order ->
                viewModelScope.launch {
                    addWorkOrderUseCase.addWorkOrder(order)
                    _canFinish.value = true
                }
            }
        }
    }

    fun updateWorkOrder() {
        val areFieldsValid = validateInput()
        if (areFieldsValid) {
            _workOrder.value?.let { order ->
                viewModelScope.launch {
                    updateWorkOrderUseCase.updateWorkOrder(order)
                    _canFinish.value = true
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var result = true
        workOrder.value?.let { order ->
            if (order.number.isBlank()) {
                _errorInputNumber.value = true
                result = false
            }
        }
        return result
    }

    fun resetErrorInputNumber() {
        _errorInputNumber.value = false
    }

    fun createWorkOrder() {
        _workOrder.value = WorkOrder(
            id = NUMBER_PREFIX + UUID.randomUUID().toString(),
            isNew = true)
    }
}
