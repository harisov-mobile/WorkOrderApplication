package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import javax.inject.Inject

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
