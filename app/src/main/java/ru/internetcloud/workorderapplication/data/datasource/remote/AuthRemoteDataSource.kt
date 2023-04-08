package ru.internetcloud.workorderapplication.data.datasource.remote

import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.AuthResponse
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor() {
    suspend fun checkAuthorization(): AuthResponse = ApiClient.getInstance().client.checkAuthorization()
}
