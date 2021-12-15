package ru.internetcloud.workorderapplication.presentation.synchro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.GetRepairTypeListUseCase

class DataSynchronizationFragmentViewModel : ViewModel() {

    private val remoteRepairTypeRepository = RemoteRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getRemoteRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepairTypeRepository)
    private val getDbRepairTypeListUseCase = GetRepairTypeListUseCase(dbRepairTypeRepository)

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _errorSynchronization = MutableLiveData<Boolean>()
    val errorSynchronization: LiveData<Boolean>
        get() = _errorSynchronization

    fun synchonizeData() {
        viewModelScope.launch {
            val remoteRepairTypeList = getRemoteRepairTypeListUseCase.getRepairTypeList()

            Log.i("rustam", " remoteRepairTypeList = " + remoteRepairTypeList.toString())

            if (remoteRepairTypeList.isEmpty()) {
                // не удалось получить данные из сервера 1С, надо проверить, есть ли данные в БД
                val dbRepairTypeList = getDbRepairTypeListUseCase.getRepairTypeList()
                Log.i("rustam", " dbRepairTypeList = " + dbRepairTypeList.toString())

                if (dbRepairTypeList.isEmpty()) {
                    _errorSynchronization.value = true
                }
            } else {

            }
        }

        _canContinue.value = true
    }
}
