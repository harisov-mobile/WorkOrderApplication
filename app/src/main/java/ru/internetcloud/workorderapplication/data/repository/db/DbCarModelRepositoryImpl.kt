package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.domain.catalog.CarModel
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import javax.inject.Inject

class DbCarModelRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val carModelMapper: CarModelMapper
) : CarModelRepository {

    override suspend fun getCarModelList(): List<CarModel> {
        return carModelMapper.fromListDbModelToListEntity(appDao.getCarModelList())
    }

    override suspend fun addCarModelList(carModelList: List<CarModel>) {
        appDao.addCarModelList(carModelMapper.fromListEntityToListDbModel(carModelList))
    }

    override suspend fun deleteAllCarModels() {
        appDao.deleteAllCarModels()
    }
}
