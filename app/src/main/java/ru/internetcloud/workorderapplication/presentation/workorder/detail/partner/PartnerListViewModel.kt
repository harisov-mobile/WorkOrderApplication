package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.db.DbPartnerRepositoryImpl
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.SearchPartnersUseCase

class PartnerListViewModel : ViewModel() {
    private val repository = DbPartnerRepositoryImpl.get()

    private val getPartnerListUseCase = GetPartnerListUseCase(repository)
    private val searchPartnersUseCase = SearchPartnersUseCase(repository)

    private val _partnerListLiveData = MutableLiveData<List<Partner>>()
    val partnerListLiveData: LiveData<List<Partner>>
        get() = _partnerListLiveData

    fun loadPartnerList() {
        viewModelScope.launch {
            _partnerListLiveData.value = getPartnerListUseCase.getPartnerList()
        }
    }

    fun searchPartners(searchText: String) {
        viewModelScope.launch {
            _partnerListLiveData.value = searchPartnersUseCase.searchPartners(searchText)
        }
    }
}
