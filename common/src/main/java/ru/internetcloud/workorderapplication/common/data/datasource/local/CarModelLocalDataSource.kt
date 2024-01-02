package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.CarModelMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.CarModel

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
