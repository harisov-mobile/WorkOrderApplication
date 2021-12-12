package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DatabaseWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.AddWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.documentoperation.UpdateWorkOrderUseCase

class WorkOrderViewModel : ViewModel() {

    // private val repository = LocalWorkOrderRepositoryImpl // требуется инъекция зависимостей!!!
    private val databaseRepository = DatabaseWorkOrderRepositoryImpl.get()
    private val remoteRepository = RemoteRepairTypeRepositoryImpl.get()

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getWorkOrderUseCase = GetWorkOrderUseCase(databaseRepository)
    private val addWorkOrderUseCase = AddWorkOrderUseCase(databaseRepository)
    private val updateWorkOrderUseCase = UpdateWorkOrderUseCase(databaseRepository)
    private val getRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepository)

    // LiveData-объекты, с помощью которых будет отображение данных в элементах управления:
    private val _workOrder = MutableLiveData<WorkOrder>()
    val workOrder: LiveData<WorkOrder>
        get() = _workOrder

    private val _repairTypes = MutableLiveData<List<RepairType>>()
    val repairTypes: LiveData<List<RepairType>>
        get() = _repairTypes

    // можно ли завершить (или закрыть)
    private val _canFinish = MutableLiveData<Unit>()
    val canFinish: LiveData<Unit>
        get() = _canFinish

    // для обработки ошибок:
    private val _errorInputNumber = MutableLiveData<Boolean>()
    val errorInputNumber: LiveData<Boolean>
        get() = _errorInputNumber

    // -------------------------------------------------------------------------------
    fun loadWorkOrder(workOrderId: Int) {
        viewModelScope.launch {
            val order = getWorkOrderUseCase.getWorkOrder(workOrderId)
            order?.let {
                _workOrder.value = it
            }
        }
    }

   fun loadRepairTypes() {
        viewModelScope.launch {
            Log.i("rustam", "вошли в loadRepairTypes")
            val order = getRepairTypeListUseCase.getRepairTypeList()
            order?.let {
                _repairTypes.value = it
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
                _canFinish.value = Unit
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
                    _canFinish.value = Unit
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
