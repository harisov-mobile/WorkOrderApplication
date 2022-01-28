package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class LoadDefaultWorkOrderSettingsUseCase(private val synchroRepository: SynchroRepository) {
    suspend fun loadDefaultWorkOrderSettings(): Boolean {
        return synchroRepository.loadDefaultWorkOrderSettings()
    }
}
