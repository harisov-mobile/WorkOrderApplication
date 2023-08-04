package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.model.document.DefaultWorkOrderSettings

interface DefaultWorkOrderSettingsRepository {

    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings?

    suspend fun deleteAllDefaultWorkOrderSettings()
}
