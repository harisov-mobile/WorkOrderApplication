package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.domain.model.catalog.CarModel
import javax.inject.Inject

class CarModelLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val carModelMapper: CarModelMapper
) {

    suspend fun getCarModelList(): List<CarModel> {
        return carModelMapper.fromListDbModelToListEntity(appDao.getCarModelList())
    }

    suspend fun addCarModelList(carModelList: List<CarModel>) {
        appDao.addCarModelList(carModelMapper.fromListEntityToListDbModel(carModelList))
    }

    suspend fun deleteAllCarModels() {
        appDao.deleteAllCarModels()
    }
}
