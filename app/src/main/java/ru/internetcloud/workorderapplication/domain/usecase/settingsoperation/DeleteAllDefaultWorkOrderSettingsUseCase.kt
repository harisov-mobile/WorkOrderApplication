package ru.internetcloud.workorderapplication.domain.usecase.settingsoperation

import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository

class DeleteAllDefaultWorkOrderSettingsUseCase(private val settingsRepository: DefaultWorkOrderSettingsRepository) {
    suspend fun deleteAllDefaultWorkOrderSettings() {
        return settingsRepository.deleteAllDefaultWorkOrderSettings()
    }
}
