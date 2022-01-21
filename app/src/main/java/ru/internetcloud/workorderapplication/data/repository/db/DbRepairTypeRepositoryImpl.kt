package ru.internetcloud.workorderapplication.data.repository.db

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class DbRepairTypeRepositoryImpl private constructor(application: Application) : RepairTypeRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val repairTypeMapper = RepairTypeMapper()

    companion object {
        private var instance: DbRepairTypeRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DbRepairTypeRepositoryImpl(application)
            }
        }

        fun get(): DbRepairTypeRepositoryImpl {
            return instance ?: throw RuntimeException("DbRepairTypeRepositoryImpl must be initialized.")
        }
    }

    override suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.getRepairTypeList())
    }

    override suspend fun addRepairType(repairType: RepairType) {
        appDao.addRepairType(repairTypeMapper.fromEntityToDbModel(repairType))
    }

    override suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
            appDao.addRepairTypeList(repairTypeMapper.fromListEntityToListDbModel(repairTypeList))
    }

    override suspend fun getRepairType(id: String): RepairType? {

        var repairType: RepairType? = null

        val repairTypeDbModel = appDao.getRepairType(id)

        repairTypeDbModel?.let {
            repairType = repairTypeMapper.fromDbModelToEntity(it)
        }

        return repairType
    }

    override suspend fun deleteAllRepairTypes() {
        appDao.deleteAllRepairTypes()
    }

    override suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.searhRepairTypes("%$searchText%"))
    }
}
