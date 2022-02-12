package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings

interface DefaultWorkOrderSettingsRepository {

    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings?

    suspend fun deleteAllDefaultWorkOrderSettings()
}
