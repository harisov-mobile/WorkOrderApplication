package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.CarJob

interface CarJobRepository {

    suspend fun getCarJobList(): List<CarJob>

    suspend fun addCarJobList(carJobList: List<CarJob>)

    suspend fun addCarJob(carJob: CarJob)

    suspend fun getCarJob(id: String): CarJob?

    suspend fun deleteAllCarJobs()

    suspend fun searchCarJobs(searchText: String): List<CarJob>
}
