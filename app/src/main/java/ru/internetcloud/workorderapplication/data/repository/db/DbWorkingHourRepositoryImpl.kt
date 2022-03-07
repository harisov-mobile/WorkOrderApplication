package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class DbWorkingHourRepositoryImpl @Inject constructor(
    // application: Application
    private val appDao: AppDao,
    private val workingHourMapper: WorkingHourMapper
) : WorkingHourRepository {

    companion object {
//        private var instance: DbWorkingHourRepositoryImpl? = null
//
//        fun initialize(application: Application) {
//            if (instance == null) {
//                instance = DbWorkingHourRepositoryImpl(application)
//            }
//        }
//
//        fun get(): DbWorkingHourRepositoryImpl {
//            return instance ?: throw RuntimeException("DbWorkingHourRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourMapper.fromListDbModelToListEntity(appDao.getWorkingHourList())
    }

    override suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        appDao.addWorkingHourList(workingHourMapper.fromListEntityToListDbModel(workingHourList))
    }

    override suspend fun getWorkingHour(id: String): WorkingHour? {
        var workingHour: WorkingHour? = null

        val workingHourDbModel = appDao.getWorkingHour(id)

        workingHourDbModel?.let {
            workingHour = workingHourMapper.fromDbModelToEntity(it)
        }

        return workingHour
    }

    override suspend fun deleteAllWorkingHours() {
        appDao.deleteAllWorkingHours()
    }

    override suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourMapper.fromListDbModelToListEntity(appDao.searhWorkingHours("%$searchText%"))
    }
}
