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
