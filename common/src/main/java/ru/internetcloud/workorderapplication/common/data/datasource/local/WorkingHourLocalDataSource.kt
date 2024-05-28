package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour

class WorkingHourLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val workingHourMapper: WorkingHourMapper
) {

    suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourMapper.fromListDbModelToListEntity(appDao.getWorkingHourList())
    }

    suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        appDao.addWorkingHourList(workingHourMapper.fromListEntityToListDbModel(workingHourList))
    }

    suspend fun getWorkingHour(id: String): WorkingHour? {
        var workingHour: WorkingHour? = null

        val workingHourDbModel = appDao.getWorkingHour(id)

        workingHourDbModel?.let {
            workingHour = workingHourMapper.fromDbModelToEntity(it)
        }

        return workingHour
    }

    suspend fun deleteAllWorkingHours() {
        appDao.deleteAllWorkingHours()
    }

    suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourMapper.fromListDbModelToListEntity(appDao.searhWorkingHours("%$searchText%"))
    }
}
