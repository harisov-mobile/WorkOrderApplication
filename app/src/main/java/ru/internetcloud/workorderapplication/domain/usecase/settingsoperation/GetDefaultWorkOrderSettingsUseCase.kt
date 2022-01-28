package ru.internetcloud.workorderapplication.domain.usecase.settingsoperation

import ru.internetcloud.workorderapplication.domain.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository

class GetDefaultWorkOrderSettingsUseCase(private val settingsRepository: DefaultWorkOrderSettingsRepository) {
    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings {
        return settingsRepository.getDefaultWorkOrderSettings()
    }
}
