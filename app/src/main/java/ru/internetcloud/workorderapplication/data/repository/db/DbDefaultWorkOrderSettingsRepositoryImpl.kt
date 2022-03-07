package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository
import javax.inject.Inject

class DbDefaultWorkOrderSettingsRepositoryImpl @Inject constructor(
    // application: Application
    private val appDao: AppDao,
    private val defaultWorkOrderSettingsMapper: DefaultWorkOrderSettingsMapper
) : DefaultWorkOrderSettingsRepository {

    companion object {
//        private var instance: DbDefaultWorkOrderSettingsRepositoryImpl? = null
//
//        fun initialize(application: Application) {
//            if (instance == null) {
//                instance = DbDefaultWorkOrderSettingsRepositoryImpl(application)
//            }
//        }
//
//        fun get(): DbDefaultWorkOrderSettingsRepositoryImpl {
//            return instance ?: throw RuntimeException("DbDefaultWorkOrderSettingsRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings? {
        var defaultWorkOrderSettings: DefaultWorkOrderSettings? = null

        val defaultWorkOrderSettingsDbModel = appDao.getDefaultWorkOrderSettings()

        defaultWorkOrderSettingsDbModel?.let {
            val defaultWorkOrderSettingsWithRequisities = appDao.getDefaultWorkOrderSettings()
            defaultWorkOrderSettings =
                defaultWorkOrderSettingsMapper.fromDbModelToEntityWithNull(defaultWorkOrderSettingsWithRequisities)
        }

        return defaultWorkOrderSettings
    }

    override suspend fun deleteAllDefaultWorkOrderSettings() {
        appDao.deleteAllDefaultWorkOrderSettings()
    }
}
