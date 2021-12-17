package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.CarJobMapper
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class DbCarJobRepositoryImpl private constructor(application: Application) : CarJobRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val carJobMapper = CarJobMapper()

    companion object {
        private var instance: DbCarJobRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DbCarJobRepositoryImpl(application)
            }
        }

        fun get(): DbCarJobRepositoryImpl {
            return instance ?: throw RuntimeException("DbCarJobRepositoryImpl must be initialized.")
        }
    }

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
}
