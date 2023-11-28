package ru.internetcloud.workorderapplication.presentation.workorder.detail.car

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListByOwnerUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.SearchCarsByOwnerUseCase

@HiltViewModel
class CarListViewModel @Inject constructor(
    private val getCarListByOwnerUseCase: GetCarListByOwnerUseCase,
    private val searchCarsByOwnerUseCase: SearchCarsByOwnerUseCase
) : ViewModel() {

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
