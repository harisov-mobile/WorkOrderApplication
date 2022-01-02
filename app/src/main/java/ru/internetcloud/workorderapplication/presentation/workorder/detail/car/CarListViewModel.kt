package ru.internetcloud.workorderapplication.presentation.workorder.detail.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.DbCarRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.SearchCarsUseCase

class CarListViewModel : ViewModel() {
    private val repository = DbCarRepositoryImpl.get()

    private val getCarListUseCase = GetCarListUseCase(repository)
    private val searchCarsUseCase = SearchCarsUseCase(repository)

    private val _carListLiveData = MutableLiveData<List<Car>>()
    val carListLiveData: LiveData<List<Car>>
        get() = _carListLiveData

    fun loadCarList() {
        viewModelScope.launch {
            _carListLiveData.value = getCarListUseCase.getCarList()
        }
    }

    fun searchCars(searchText: String) {
        viewModelScope.launch {
            _carListLiveData.value = searchCarsUseCase.searchCars(searchText)
        }
    }
}
