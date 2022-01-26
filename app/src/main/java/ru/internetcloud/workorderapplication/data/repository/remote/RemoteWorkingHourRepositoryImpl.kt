package ru.internetcloud.workorderapplication.data.repository.remote

import android.app.Application
import android.util.Log
import ru.internetcloud.workorderapplication.data.mapper.WorkingHourMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.WorkingHourResponse
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

class RemoteWorkingHourRepositoryImpl private constructor() : WorkingHourRepository {

    companion object {
        private var instance: RemoteWorkingHourRepositoryImpl? = null

        fun initialize() {
            if (instance == null) {
                instance = RemoteWorkingHourRepositoryImpl()
            }
        }

        fun get(): RemoteWorkingHourRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteWorkingHourRepositoryImpl must be initialized.")
        }
    }

    private val workingHourMapper = WorkingHourMapper()

    override suspend fun getWorkingHourList(): List<WorkingHour> {
        var workingHourResponse = WorkingHourResponse(emptyList())

        try {
            workingHourResponse = ApiClient.getInstance().client.getWorkingHours()
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "ошибка при загрузке getWorkingHours" + e.toString())
        }

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
