package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import javax.inject.Inject

class DbCarJobRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val carJobMapper: CarJobMapper
) : CarJobRepository {

    override suspend fun getCarJobList(): List<CarJob> {
        return carJobMapper.fromListDbModelToListEntity(appDao.getCarJobList())
    }

    override suspend fun addCarJobList(carJobList: List<CarJob>) {
        appDao.addCarJobList(carJobMapper.fromListEntityToListDbModel(carJobList))
    }

    override suspend fun addCarJob(carJob: CarJob) {
        appDao.addCarJob(carJobMapper.fromEntityToDbModel(carJob))
    }

    override suspend fun getCarJob(id: String): CarJob? {
        var carJob: CarJob? = null

        val carJobDbModel = appDao.getCarJob(id)

        carJobDbModel?.let {
            carJob = carJobMapper.fromDbModelToEntity(it)
        }

        return carJob
    }

    override suspend fun deleteAllCarJobs() {
        appDao.deleteAllCarJobs()
    }

    override suspend fun searchCarJobs(searchText: String): List<CarJob> {
        return carJobMapper.fromListDbModelToListEntity(appDao.searhCarJobs("%$searchText%"))
    }
}
