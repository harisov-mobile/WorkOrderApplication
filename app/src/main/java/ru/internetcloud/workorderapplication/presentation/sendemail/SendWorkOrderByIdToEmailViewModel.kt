package ru.internetcloud.workorderapplication.presentation.sendemail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.SendWorkOrderToEmailUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.UploadWorkOrderByIdUseCase

class SendWorkOrderByIdToEmailViewModel @Inject constructor(
    private val uploadWorkOrderByIdUseCase: UploadWorkOrderByIdUseCase,
    private val sendWorkOrderToEmailUseCase: SendWorkOrderToEmailUseCase
) : ViewModel() {

//    // Репозитории
//    private val synchroRepositoryImpl = SynchroRepositoryImpl.get() // требуется инъекция зависимостей!!!

    var id: String = ""
    var email: String = ""

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _currentSituation = MutableLiveData<Int>()
    val currentSituation: LiveData<Int>
        get() = _currentSituation

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun sendWorkOrderToEmailById(id: String, email: String) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _currentSituation.value = R.string.wait

            val result = uploadWorkOrderByIdUseCase.uploadWorkOrderById(id)

            if (result.isSuccess) {
                val sendResult = sendWorkOrderToEmailUseCase.sendWorkOrderToEmail(id, email)
                if (sendResult.isSuccess) {
                    _currentSituation.value = R.string.success_send_work_order
                } else {
                    _errorMessage.value = sendResult.errorMessage
                    _currentSituation.value = R.string.fail_send_work_order
                }
                _canContinue.value = true
            } else {
                _errorMessage.value = result.errorMessage
                _currentSituation.value = R.string.fail_send_work_order
                _canContinue.value = true
            }
        }
    }
}
