package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
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

//        try {
//            repairTypeResponse = ApiClientOld.apiClient.getRepairTypes()
//        } catch (e: Exception) {
//            Log.i("rustam", e.toString())
//        }
//
//        Log.i("rustam", repairTypeResponse.toString())

        return repairTypeMapper.fromListDtoToListEntity(repairTypeResponse.repairtypes)
    }

    override fun getRepairType(id: Int): RepairType? {
        TODO("Not yet implemented")
    }

    override fun getRepairTypeById1C(id1C: String): RepairType? {
        TODO("Not yet implemented")
    }
}
