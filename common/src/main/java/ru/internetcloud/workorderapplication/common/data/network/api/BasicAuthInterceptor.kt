package ru.internetcloud.workorderapplication.common.data.network.api

import java.nio.charset.StandardCharsets
import okhttp3.Credentials
import okhttp3.Interceptor

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private var credentials: String = Credentials.basic(username, password, charset = StandardCharsets.UTF_8)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}
