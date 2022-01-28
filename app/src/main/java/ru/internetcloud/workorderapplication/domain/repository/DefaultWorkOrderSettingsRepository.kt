package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings

interface DefaultWorkOrderSettingsRepository {

    suspend fun getDefaultWorkOrderListSettings(): List<DefaultWorkOrderSettings>

    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings
}
