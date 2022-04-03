package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.Partner

interface PartnerRepository {

    suspend fun getPartnerList(): List<Partner>

    suspend fun addPartnerList(partnerList: List<Partner>)

    suspend fun getPartner(id: String): Partner?

    suspend fun deleteAllPartners()

    suspend fun searchPartners(searchText: String): List<Partner>
}
