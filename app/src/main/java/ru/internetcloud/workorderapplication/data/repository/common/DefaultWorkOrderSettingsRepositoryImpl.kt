package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.datasource.local.DefaultWorkOrderSettingsLocalDataSource
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository

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
