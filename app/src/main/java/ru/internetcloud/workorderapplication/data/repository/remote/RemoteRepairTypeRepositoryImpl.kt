package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class RemoteRepairTypeRepositoryImpl @Inject constructor(
    private val repairTypeMapper: RepairTypeMapper
) : RepairTypeRepository {

    companion object {
//        private var instance: RemoteRepairTypeRepositoryImpl? = null
//
//        fun initialize() {
//            if (instance == null) {
//                instance = RemoteRepairTypeRepositoryImpl()
//            }
//        }
//
//        fun get(): RemoteRepairTypeRepositoryImpl {
//            return instance ?: throw RuntimeException("RemoteRepairTypeRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getRepairTypeList(): List<RepairType> {

        var repairTypeResponse = RepairTypeResponse(emptyList())

        try {
            repairTypeResponse = ApiClient.getInstance().client.getRepairTypes()
        } catch (e: Exception) {
            // ничего не делаю
        }

        return repairTypeMapper.fromListDtoToListEntity(repairTypeResponse.repairtypes)
    }

    override suspend fun addRepairType(repairType: RepairType) {
        throw RuntimeException("Error - method addRepairType is restricted in RemoteRepairTypeRepositoryImpl")
    }

    override suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
        throw RuntimeException("Error - method addRepairTypeList is restricted in RemoteRepairTypeRepositoryImpl")
    }

    override suspend fun getRepairType(id: String): RepairType? {
        throw RuntimeException("Error - method getRepairType is restricted in RemoteRepairTypeRepositoryImpl")
    }

    override suspend fun deleteAllRepairTypes() {
        throw RuntimeException("Error - method deleteAllRepairTypes is restricted in RemoteRepairTypeRepositoryImpl")
    }

    override suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        throw RuntimeException("Error - method searchRepairTypes is restricted in RemoteRepairTypeRepositoryImpl")
    }
}
