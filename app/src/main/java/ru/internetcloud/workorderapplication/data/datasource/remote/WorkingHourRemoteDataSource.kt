package ru.internetcloud.workorderapplication.data.datasource.remote

import ru.internetcloud.workorderapplication.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.model.catalog.WorkingHour
import javax.inject.Inject

class WorkingHourRemoteDataSource @Inject constructor(
    private val workingHourMapper: WorkingHourMapper
) {

    suspend fun getWorkingHourList(): List<WorkingHour> {
        val workingHourResponse = ApiClient.getInstance().client.getWorkingHours()
        return workingHourMapper.fromListDtoToListEntity(workingHourResponse.workingHours)
    }
}
