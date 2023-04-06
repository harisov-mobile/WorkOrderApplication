package ru.internetcloud.workorderapplication.data.datasource.remote

import ru.internetcloud.workorderapplication.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import javax.inject.Inject

class DefaultWorkOrderSettingsRemoteDataSource @Inject constructor(
    private val defMapper: DefaultWorkOrderSettingsMapper
) {

    suspend fun getDefaultWorkOrderSettings(): List<DefaultWorkOrderSettingsDbModel> {
        val defResponse = ApiClient.getInstance().client.getDefaultWorkOrderSettings()
        return defMapper.fromListDtoToListDbModel(defResponse.settings)
    }
}
