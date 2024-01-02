package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarJob

interface CarJobRepository {

    suspend fun getCarJobList(): List<CarJob>

    suspend fun addCarJobList(carJobList: List<CarJob>)

    suspend fun addCarJob(carJob: CarJob)

    suspend fun deleteAllCarJobs()

    suspend fun searchCarJobs(searchText: String): List<CarJob>
}
