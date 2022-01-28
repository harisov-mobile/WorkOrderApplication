package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.CarJobResponse
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class RemoteCarJobRepositoryImpl private constructor() : CarJobRepository {

    companion object {
        private var instance: RemoteCarJobRepositoryImpl? = null

        fun initialize() {
            if (instance == null) {
                instance = RemoteCarJobRepositoryImpl()
            }
        }

        fun get(): RemoteCarJobRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteCarJobRepositoryImpl must be initialized.")
        }
    }

    private val carJobMapper = CarJobMapper()

    override suspend fun getCarJobList(): List<CarJob> {
        var carJobResponse = CarJobResponse(emptyList())

        try {
            carJobResponse = ApiClient.getInstance().client.getCarJobs()
        } catch (e: Exception) {
            // ничего не делаю
        }
        return carJobMapper.fromListDtoToListEntity(carJobResponse.carJobs)
    }

    override suspend fun addCarJobList(carJobList: List<CarJob>) {
        throw RuntimeException("Error - method addCarJobList is restricted in RemoteCarJobRepositoryImpl")
    }

    override suspend fun addCarJob(carJob: CarJob) {
        throw RuntimeException("Error - method addCarJob is restricted in RemoteCarJobRepositoryImpl")
    }

    override suspend fun getCarJob(id: String): CarJob? {
        throw RuntimeException("Error - method getCarJob is restricted in RemoteCarJobRepositoryImpl")
    }

    override suspend fun deleteAllCarJobs() {
        throw RuntimeException("Error - method deleteAllCarJobs is restricted in RemoteCarJobRepositoryImpl")
    }

    override suspend fun searchCarJobs(searchText: String): List<CarJob> {
        throw RuntimeException("Error - method searchCarJobs is restricted in RemoteCarJobRepositoryImpl")
    }
}
