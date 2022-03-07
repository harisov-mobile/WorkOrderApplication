package ru.internetcloud.workorderapplication.domain.usecase.settingsoperation

import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository
import javax.inject.Inject

class DeleteAllDefaultWorkOrderSettingsUseCase @Inject constructor(private val settingsRepository: DefaultWorkOrderSettingsRepository) {

    suspend fun deleteAllDefaultWorkOrderSettings() {
        return settingsRepository.deleteAllDefaultWorkOrderSettings()
    }
}
