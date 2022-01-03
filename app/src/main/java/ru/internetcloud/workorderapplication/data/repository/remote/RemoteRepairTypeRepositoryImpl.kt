package ru.internetcloud.workorderapplication.data.repository.remote

import android.app.Application
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.RepairTypeResponse
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class RemoteRepairTypeRepositoryImpl private constructor(application: Application) : RepairTypeRepository {

    companion object {
        private var instance: RemoteRepairTypeRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = RemoteRepairTypeRepositoryImpl(application)
            }
        }

        fun get(): RemoteRepairTypeRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteRepairTypeRepositoryImpl must be initialized.")
        }
    }

    private val repairTypeMapper = RepairTypeMapper()

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

    override suspend fun getRepairType(id: String): RepairType? {
        throw RuntimeException("Error - method getRepairType is restricted in RemoteRepairTypeRepositoryImpl")
        return null
    }

    override suspend fun deleteAllRepairTypes() {
        throw RuntimeException("Error - method deleteAllRepairTypes is restricted in RemoteRepairTypeRepositoryImpl")
    }

    override suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        throw RuntimeException("Error - method searchRepairTypes is restricted in RemoteRepairTypeRepositoryImpl")
    }
}
