package ru.internetcloud.workorderapplication.common.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.datasource.local.DefaultWorkOrderSettingsLocalDataSource
import ru.internetcloud.workorderapplication.common.domain.model.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.common.domain.repository.DefaultWorkOrderSettingsRepository

class DefaultWorkOrderSettingsRepositoryImpl @Inject constructor(
    private val localDataSource: DefaultWorkOrderSettingsLocalDataSource
) : DefaultWorkOrderSettingsRepository {

    override suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings? {
        return localDataSource.getDefaultWorkOrderSettings()
    }

    override suspend fun deleteAllDefaultWorkOrderSettings() {
        localDataSource.deleteAllDefaultWorkOrderSettings()
    }
}
