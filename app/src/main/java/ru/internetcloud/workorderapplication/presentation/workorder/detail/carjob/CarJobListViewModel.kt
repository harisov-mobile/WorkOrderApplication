package ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.db.DbCarJobRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.SearchCarJobsUseCase

class CarJobListViewModel : ViewModel() {

    var selectedCarJob: CarJob? = null

    private val repository = DbCarJobRepositoryImpl.get()

    private val getCarJobListUseCase = GetCarJobListUseCase(repository)
    private val searchCarJobsUseCase = SearchCarJobsUseCase(repository)

    private val _carJobListLiveData = MutableLiveData<List<CarJob>>()
    val carJobListLiveData: LiveData<List<CarJob>>
        get() = _carJobListLiveData

    fun loadCarJobList() {
        viewModelScope.launch {
            _carJobListLiveData.value = getCarJobListUseCase.getCarJobList()
        }
    }

    fun searchCarJobs(searchText: String) {
        viewModelScope.launch {
            _carJobListLiveData.value = searchCarJobsUseCase.searchCarJobs(searchText)
        }
    }
}
