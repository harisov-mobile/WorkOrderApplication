package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class TestPartnerRepository : PartnerRepository {
    override suspend fun getPartnerList(): List<Partner> {
        TODO("Not yet implemented")
    }

    override suspend fun addPartnerList(partnerList: List<Partner>) {
        TODO("Not yet implemented")
    }

    override suspend fun getPartner(id: String): Partner? {
        return Partner(
            id = id,
            name = "Test_partner"
        )
    }

    override suspend fun deleteAllPartners() {
        TODO("Not yet implemented")
    }

    override suspend fun searchPartners(searchText: String): List<Partner> {
        TODO("Not yet implemented")
    }
}

class GetPartnerUseCaseTest {

    @Test
    fun `simple test`() {

        val partnerRepository = TestPartnerRepository()

        val getPartnerUseCase = GetPartnerUseCase(partnerRepository)

        val id = "1"

        GlobalScope.launch {
            val actual = getPartnerUseCase.getPartner(id)

            val expected = Partner(
                id = "222",
                name = "Test_partner222")

            assertEquals(expected, actual)
        }
    }
}
