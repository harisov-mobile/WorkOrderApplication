package ru.internetcloud.workorderapplication.presentation.workorder.detail.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.db.DbCarRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListByOwnerUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.SearchCarsByOwnerUseCase

class CarListViewModel : ViewModel() {
    private val repository = DbCarRepositoryImpl.get()

    private val getCarListByOwnerUseCase = GetCarListByOwnerUseCase(repository)
    private val searchCarsByOwnerUseCase = SearchCarsByOwnerUseCase(repository)

    var partner: Partner? = null
    var selectedCar: Car? = null

    private val _carListLiveData = MutableLiveData<List<Car>>()
    val carListLiveData: LiveData<List<Car>>
        get() = _carListLiveData

    fun loadCarList() {
        viewModelScope.launch {
            partner?.let {
                _carListLiveData.value = getCarListByOwnerUseCase.getCarListByOwner(it.id)
            }
        }
    }

    fun searchCarsByOwner(searchText: String, ownerId: String) {
        viewModelScope.launch {
            _carListLiveData.value = searchCarsByOwnerUseCase.searchCarsByOwner(searchText, ownerId)
        }
    }
}
