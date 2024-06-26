package ru.internetcloud.workorderapplication.workorderdetail.presentation.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.partner.SearchPartnersUseCase

@HiltViewModel
class PartnerListViewModel @Inject constructor(
    private val getPartnerListUseCase: GetPartnerListUseCase,
    private val searchPartnersUseCase: SearchPartnersUseCase
) : ViewModel() {

    var selectedPartner: Partner? = null

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
