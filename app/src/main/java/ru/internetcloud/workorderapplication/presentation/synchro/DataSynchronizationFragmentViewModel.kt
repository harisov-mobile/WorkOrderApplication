package ru.internetcloud.workorderapplication.presentation.synchro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DbCarJobRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteCarJobRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.AddCarJobUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.DeleteCarJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.AddRepairTypeUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.DeleteRepairTypesUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase

class DataSynchronizationFragmentViewModel : ViewModel() {

    private val remoteRepairTypeRepository = RemoteRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbRepairTypeRepository = DbRepairTypeRepositoryImpl.get() // требуется инъекция зависимостей!!!

    private val remoteCarJobRepository = RemoteCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
    private val dbCarJobRepository = DbCarJobRepositoryImpl.get() // требуется инъекция зависимостей!!!
    
    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val getRemoteRepairTypeListUseCase = GetRepairTypeListUseCase(remoteRepairTypeRepository)
    private val getDbRepairTypeListUseCase = GetRepairTypeListUseCase(dbRepairTypeRepository)
    private val addDbRepairTypeUseCase = AddRepairTypeUseCase(dbRepairTypeRepository)
    private val deleteRepairTypesUseCase = DeleteRepairTypesUseCase(dbRepairTypeRepository)

    private val getRemoteCarJobListUseCase = GetCarJobListUseCase(remoteCarJobRepository)
    private val getDbCarJobListUseCase = GetCarJobListUseCase(dbCarJobRepository)
    private val addDbCarJobUseCase = AddCarJobUseCase(dbCarJobRepository)
    private val deleteCarJobsUseCase = DeleteCarJobsUseCase(dbCarJobRepository)

    
    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _canContinueWithoutSynchro = MutableLiveData<Boolean>()
    val canContinueWithoutSynchro: LiveData<Boolean>
        get() = _canContinueWithoutSynchro

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
                } else {
                    _canContinueWithoutSynchro.value = true
                }
            } else {
                deleteRepairTypesUseCase.deleteAllRepairTypes()
                remoteRepairTypeList.forEach {
                    addDbRepairTypeUseCase.addRepairType(it)
                }

                refreshCarJob()

                _canContinue.value = true
            }
        }
    }

    suspend fun refreshCarJob() {
        val remoteCarJobList = getRemoteCarJobListUseCase.getCarJobList()
        if (!remoteCarJobList.isEmpty()) {
            deleteCarJobsUseCase.deleteAllCarJobs()
            remoteCarJobList.forEach {
                addDbCarJobUseCase.addCarJob(it)
            }
        }
    }

    suspend fun refreshDepartment() {
//        val remoteRepairTypeList = getRemoteRepairTypeListUseCase.getRepairTypeList()
//        if (!remoteRepairTypeList.isEmpty()) {
//            deleteRepairTypesUseCase.deleteAllRepairTypes()
//            remoteRepairTypeList.forEach {
//                addDbRepairTypeUseCase.addRepairType(it)
//            }
//        }
    }
}
