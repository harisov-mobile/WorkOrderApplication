package ru.internetcloud.workorderapplication.data.network.api

import okhttp3.Credentials
import okhttp3.Interceptor
import java.nio.charset.StandardCharsets

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private var credentials: String = Credentials.basic(username, password, charset = StandardCharsets.UTF_8)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}
