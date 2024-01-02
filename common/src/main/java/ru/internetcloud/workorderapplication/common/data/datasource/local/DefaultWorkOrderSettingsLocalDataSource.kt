package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.common.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.common.domain.model.document.DefaultWorkOrderSettings

class DefaultWorkOrderSettingsLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val defaultWorkOrderSettingsMapper: DefaultWorkOrderSettingsMapper
) {

    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings? {
        var defaultWorkOrderSettings: DefaultWorkOrderSettings? = null

        val defaultDbModel = appDao.getDefaultWorkOrderSettings()

        defaultDbModel?.let { defSetting ->
            defaultWorkOrderSettings = defaultWorkOrderSettingsMapper.fromDbModelToEntityWithNull(defSetting)
        }

        return defaultWorkOrderSettings
    }

    suspend fun deleteAllDefaultWorkOrderSettings() {
        appDao.deleteAllDefaultWorkOrderSettings()
    }

    suspend fun addDefaultWorkOrderSettingsList(list: List<DefaultWorkOrderSettingsDbModel>) {
        appDao.addDefaultWorkOrderSettingsList(list)
    }
}
