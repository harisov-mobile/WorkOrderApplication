package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.CarJobLocalDataSource
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class CarJobRepositoryImpl @Inject constructor(
    private val carJobLocalDataSource: CarJobLocalDataSource
) : CarJobRepository {

    override suspend fun getCarJobList(): List<CarJob> {
        return carJobLocalDataSource.getCarJobList()
    }

    override suspend fun addCarJobList(carJobList: List<CarJob>) {
        carJobLocalDataSource.addCarJobList(carJobList)
    }

    override suspend fun addCarJob(carJob: CarJob) {
        carJobLocalDataSource.addCarJob(carJob)
    }

    override suspend fun deleteAllCarJobs() {
        carJobLocalDataSource.deleteAllCarJobs()
    }

    override suspend fun searchCarJobs(searchText: String): List<CarJob> {
        return carJobLocalDataSource.searchCarJobs(searchText)
    }
}
