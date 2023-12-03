package ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.model.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.SearchCarJobsUseCase

@HiltViewModel
class CarJobListViewModel @Inject constructor(
    private val getCarJobListUseCase: GetCarJobListUseCase,
    private val searchCarJobsUseCase: SearchCarJobsUseCase
) : ViewModel() {

    var selectedCarJob: CarJob? = null

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
