package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class RemoteWorkingHourRepositoryImpl @Inject constructor(
    private val workingHourMapper: WorkingHourMapper
) : WorkingHourRepository {

    override suspend fun getWorkingHourList(): List<WorkingHour> {
        val workingHourResponse = ApiClient.getInstance().client.getWorkingHours()
        return workingHourMapper.fromListDtoToListEntity(workingHourResponse.workingHours)
    }

    override suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        throw RuntimeException("Error - method addWorkingHourList is restricted in RemoteWorkingHourRepositoryImpl")
    }

    override suspend fun getWorkingHour(id: String): WorkingHour? {
        throw RuntimeException("Error - method getWorkingHour is restricted in RemoteWorkingHourRepositoryImpl")
    }

    override suspend fun deleteAllWorkingHours() {
        throw RuntimeException("Error - method deleteAllWorkingHours is restricted in RemoteWorkingHourRepositoryImpl")
    }

    override suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        throw RuntimeException("Error - method searchWorkingHours is restricted in RemoteWorkingHourRepositoryImpl")
    }
}
