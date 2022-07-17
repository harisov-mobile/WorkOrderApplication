package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.network.api.ApiClient

class DefaultWorkOrderSettingsRemoteDataSource @Inject constructor(
    private val defMapper: DefaultWorkOrderSettingsMapper
) {

    suspend fun getDefaultWorkOrderSettings(): List<DefaultWorkOrderSettingsDbModel> {
        val defResponse = ApiClient.getInstance().client.getDefaultWorkOrderSettings()
        return defMapper.fromListDtoToListDbModel(defResponse.settings)
    }
}
