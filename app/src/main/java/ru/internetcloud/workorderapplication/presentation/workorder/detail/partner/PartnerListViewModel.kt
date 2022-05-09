package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetPartnerListUseCaseQualifier
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.SearchPartnersUseCase
import javax.inject.Inject

class PartnerListViewModel @Inject constructor(
    @DbGetPartnerListUseCaseQualifier
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
