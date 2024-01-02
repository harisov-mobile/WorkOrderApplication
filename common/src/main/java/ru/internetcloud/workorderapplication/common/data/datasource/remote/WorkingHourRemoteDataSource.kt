package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour

class WorkingHourRemoteDataSource @Inject constructor(
    private val workingHourMapper: WorkingHourMapper
) {

    suspend fun getWorkingHourList(): List<WorkingHour> {
        val workingHourResponse = ApiClient.getInstance().client.getWorkingHours()
        return workingHourMapper.fromListDtoToListEntity(workingHourResponse.workingHours)
    }
}
