package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.DefaultWorkOrderSettingsMapper
import ru.internetcloud.workorderapplication.common.data.model.DefaultWorkOrderSettingsDbModel
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient

class DefaultWorkOrderSettingsRemoteDataSource @Inject constructor(
    private val defMapper: DefaultWorkOrderSettingsMapper
) {

    suspend fun getDefaultWorkOrderSettings(): List<DefaultWorkOrderSettingsDbModel> {
        val defResponse = ApiClient.getInstance().client.getDefaultWorkOrderSettings()
        return defMapper.fromListDtoToListDbModel(defResponse.settings)
    }
}
