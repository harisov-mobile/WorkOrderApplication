package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

interface WorkingHourRepository {

    suspend fun getWorkingHourList(): List<WorkingHour>

    suspend fun addWorkingHourList(carJobList: List<WorkingHour>)

    suspend fun getWorkingHour(id: String): WorkingHour?

    suspend fun deleteAllWorkingHours()
}
