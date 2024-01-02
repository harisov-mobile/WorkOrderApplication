package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner

interface PartnerRepository {

    suspend fun getPartnerList(): List<Partner>

    suspend fun addPartnerList(partnerList: List<Partner>)

    suspend fun deleteAllPartners()

    suspend fun searchPartners(searchText: String): List<Partner>
}
