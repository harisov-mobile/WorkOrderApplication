package ru.internetcloud.workorderapplication.presentation.synchro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase

class DataSynchronizationFragmentViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val setAuthParametersUseCase = SetAuthParametersUseCase(repository)
    private val checkAuthParametersUseCase = CheckAuthParametersUseCase(repository)

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _errorSynchronization = MutableLiveData<Boolean>()
    val errorSynchronization: LiveData<Boolean>
        get() = _errorSynchronization

    fun synchonizeData() {
        _canContinue.value = true
        //_errorSynchronization.value = true
    }
}
