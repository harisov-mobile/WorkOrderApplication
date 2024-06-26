package ru.internetcloud.workorderapplication.workorderdetail.presentation.sendemail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.usecase.synchrooperation.SendWorkOrderToEmailUseCase
import ru.internetcloud.workorderapplication.common.domain.usecase.synchrooperation.UploadWorkOrderByIdUseCase
import ru.internetcloud.workorderapplication.workorderdetail.R

@HiltViewModel
class SendWorkOrderByIdToEmailViewModel @Inject constructor(
    private val uploadWorkOrderByIdUseCase: UploadWorkOrderByIdUseCase,
    private val sendWorkOrderToEmailUseCase: SendWorkOrderToEmailUseCase
) : ViewModel() {

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
