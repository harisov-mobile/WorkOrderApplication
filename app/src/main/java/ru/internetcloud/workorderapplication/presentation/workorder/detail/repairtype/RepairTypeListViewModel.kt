package ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.SearchRepairTypesUseCase
import javax.inject.Inject

class RepairTypeListViewModel @Inject constructor(
    private val getRepairTypeListUseCase: GetRepairTypeListUseCase,
    private val searchRepairTypesUseCase: SearchRepairTypesUseCase

) : ViewModel() {

    var selectedRepairType: RepairType? = null

    private val _repairTypeListLiveData = MutableLiveData<List<RepairType>>()
    val repairTypeListLiveData: LiveData<List<RepairType>>
        get() = _repairTypeListLiveData

    fun loadRepairTypeList() {
        viewModelScope.launch {
            _repairTypeListLiveData.value = getRepairTypeListUseCase.getRepairTypeList()
        }
    }

    fun searchRepairTypes(searchText: String) {
        viewModelScope.launch {
            _repairTypeListLiveData.value = searchRepairTypesUseCase.searchRepairTypes(searchText)
        }
    }
}
