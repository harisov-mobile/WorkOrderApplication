package ru.internetcloud.workorderapplication.presentation.workorder.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DbWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.AddWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.UpdateWorkOrderUseCase

class WorkOrderViewModel : ViewModel() {

    // private val repository = LocalWorkOrderRepositoryImpl // требуется инъекция зависимостей!!!
    private val databaseRepository = DbWorkOrderRepositoryImpl.get()

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getWorkOrderUseCase = GetWorkOrderUseCase(databaseRepository)
    private val addWorkOrderUseCase = AddWorkOrderUseCase(databaseRepository)
    private val updateWorkOrderUseCase = UpdateWorkOrderUseCase(databaseRepository)

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

    // -------------------------------------------------------------------------------
    fun loadWorkOrder(workOrderId: String) {
        viewModelScope.launch {
            val order = getWorkOrderUseCase.getWorkOrder(workOrderId)
            order?.let {
                _workOrder.value = it
            }
        }
    }

    fun addWorkOrder(inputNumber: String?) {
        val number = parseNumber(inputNumber)
        val areFieldsValid = validateInput(number)
        if (areFieldsValid) {
            viewModelScope.launch {
                val order = WorkOrder(number = number)
                addWorkOrderUseCase.addWorkOrder(order)
                _canFinish.value = true
            }
        }
    }

    fun updateWorkOrder(inputNumber: String?) {
        val number = parseNumber(inputNumber)
        val areFieldsValid = validateInput(number)
        if (areFieldsValid) {
            _workOrder.value?.let {
                viewModelScope.launch {
                    it.number = number
                    updateWorkOrderUseCase.updateWorkOrder(it)
                    _canFinish.value = true
                }
            }
        }
    }

    private fun parseNumber(inputNumber: String?): String {
        return inputNumber?.trim() ?: ""
    }

    private fun validateInput(number: String): Boolean {
        var result = true
        if (number.isBlank()) {
            _errorInputNumber.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputNumber() {
        _errorInputNumber.value = false
    }
}
