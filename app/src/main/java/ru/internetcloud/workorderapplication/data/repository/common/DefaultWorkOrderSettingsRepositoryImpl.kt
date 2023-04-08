package ru.internetcloud.workorderapplication.data.repository.common

import ru.internetcloud.workorderapplication.data.datasource.local.DefaultWorkOrderSettingsLocalDataSource
import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository
import javax.inject.Inject

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
