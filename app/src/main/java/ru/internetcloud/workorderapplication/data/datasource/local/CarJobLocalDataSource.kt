package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.domain.model.catalog.CarJob
import javax.inject.Inject

class CarJobLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val carJobMapper: CarJobMapper
) {

    suspend fun getCarJobList(): List<CarJob> {
        return carJobMapper.fromListDbModelToListEntity(appDao.getCarJobList())
    }

    suspend fun addCarJobList(carJobList: List<CarJob>) {
        appDao.addCarJobList(carJobMapper.fromListEntityToListDbModel(carJobList))
    }

    suspend fun addCarJob(carJob: CarJob) {
        appDao.addCarJob(carJobMapper.fromEntityToDbModel(carJob))
    }

    suspend fun deleteAllCarJobs() {
        appDao.deleteAllCarJobs()
    }

    suspend fun searchCarJobs(searchText: String): List<CarJob> {
        return carJobMapper.fromListDbModelToListEntity(appDao.searhCarJobs("%$searchText%"))
    }
}
