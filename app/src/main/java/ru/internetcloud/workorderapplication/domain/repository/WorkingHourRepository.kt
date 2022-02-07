package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

interface WorkingHourRepository {

    suspend fun getWorkingHourList(): List<WorkingHour>

    suspend fun addWorkingHourList(workingHourList: List<WorkingHour>)

    suspend fun getWorkingHour(id: String): WorkingHour?

    suspend fun deleteAllWorkingHours()

    suspend fun searchWorkingHours(searchText: String): List<WorkingHour>
}
