package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.document.DefaultWorkOrderSettings

interface DefaultWorkOrderSettingsRepository {

    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings?

    suspend fun deleteAllDefaultWorkOrderSettings()
}
