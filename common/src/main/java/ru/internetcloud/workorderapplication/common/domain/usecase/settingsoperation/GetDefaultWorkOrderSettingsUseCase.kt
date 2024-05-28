package ru.internetcloud.workorderapplication.common.domain.usecase.settingsoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.document.DefaultWorkOrderSettings
import ru.internetcloud.workorderapplication.common.domain.repository.DefaultWorkOrderSettingsRepository

class GetDefaultWorkOrderSettingsUseCase @Inject constructor(
    private val settingsRepository: DefaultWorkOrderSettingsRepository
) {
    suspend fun getDefaultWorkOrderSettings(): DefaultWorkOrderSettings? {
        return settingsRepository.getDefaultWorkOrderSettings()
    }
}
