package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.data.network.dto.AuthResponse

class AuthRemoteDataSource @Inject constructor() {
    suspend fun checkAuthorization(): AuthResponse = ApiClient.getInstance().client.checkAuthorization()
}
