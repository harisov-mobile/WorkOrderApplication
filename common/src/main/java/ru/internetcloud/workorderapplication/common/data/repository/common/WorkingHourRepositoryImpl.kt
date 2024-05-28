package ru.internetcloud.workorderapplication.common.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.datasource.local.WorkingHourLocalDataSource
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.common.domain.repository.WorkingHourRepository

class WorkingHourRepositoryImpl @Inject constructor(
    private val workingHourLocalDataSource: WorkingHourLocalDataSource
) : WorkingHourRepository {

    override suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourLocalDataSource.getWorkingHourList()
    }

    override suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        workingHourLocalDataSource.addWorkingHourList(workingHourList)
    }

    override suspend fun getWorkingHour(id: String): WorkingHour? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWorkingHours() {
        workingHourLocalDataSource.deleteAllWorkingHours()
    }

    override suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourLocalDataSource.searchWorkingHours(searchText)
    }
}
