package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarJob

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
