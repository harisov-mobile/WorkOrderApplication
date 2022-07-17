package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.WorkingHourLocalDataSource
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

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
