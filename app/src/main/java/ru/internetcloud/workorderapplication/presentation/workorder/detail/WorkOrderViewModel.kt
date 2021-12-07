package ru.internetcloud.workorderapplication.presentation.workorder.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DatabaseWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.LocalWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.usecase.AddWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.GetWorkOrderUseCase
import ru.internetcloud.workorderapplication.domain.usecase.UpdateWorkOrderUseCase
import kotlin.random.Random

class WorkOrderViewModel : ViewModel() {

    //private val repository = LocalWorkOrderRepositoryImpl // требуется инъекция зависимостей!!!
    private val repository = DatabaseWorkOrderRepositoryImpl.get()

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getWorkOrderUseCase = GetWorkOrderUseCase(repository)
    private val addWorkOrderUseCase = AddWorkOrderUseCase(repository)
    private val updateWorkOrderUseCase = UpdateWorkOrderUseCase(repository)

    private val scope = CoroutineScope(Dispatchers.IO)

    // LiveData-объекты, с помощью которых будет отображение данных в элементах управления:
    private val _workOrder = MutableLiveData<WorkOrder>()
    val workOrder: LiveData<WorkOrder>
        get() = _workOrder

    // можно ли завершить (или закрыть)?
    private val _canFinish = MutableLiveData<Unit>()
    val canFinish: LiveData<Unit>
        get() = _canFinish

    // для обработки ошибок:
    private val _errorInputNumber = MutableLiveData<Boolean>()
    val errorInputNumber: LiveData<Boolean>
        get() = _errorInputNumber

    // -------------------------------------------------------------------------------
    fun loadWorkOrder(workOrderId: Int) {
        scope.launch {
            val order = getWorkOrderUseCase.getWorkOrder(workOrderId)
            order?.let {
                _workOrder.postValue(it)
            }
        }
    }

    fun addWorkOrder(inputNumber: String?) {
        val number = parseNumber(inputNumber)
        val areFieldsValid = validateInput(number)
        if (areFieldsValid) {
            scope.launch {
                val order = WorkOrder(number = number)
                addWorkOrderUseCase.addWorkOrder(order)
                _canFinish.postValue(Unit)
            }
        }
    }

    fun updateWorkOrder(inputNumber: String?) {
        val number = parseNumber(inputNumber)
        val areFieldsValid = validateInput(number)
        if (areFieldsValid) {
            _workOrder.value?.let {
                scope.launch {
                    it.number = number
                    updateWorkOrderUseCase.updateWorkOrder(it)
                    _canFinish.postValue(Unit)
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
