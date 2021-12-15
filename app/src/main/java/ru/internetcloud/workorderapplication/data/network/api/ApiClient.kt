package ru.internetcloud.workorderapplication.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.domain.common.AuthParameters

class ApiClient private constructor(private val authParameters: AuthParameters) {

    companion object {
        private var instance: ApiClient? = null

        fun initialize(authParameters: AuthParameters) {
            if (instance == null) {
                instance = ApiClient(authParameters)
            }
        }

        fun getInstance(): ApiClient {
            return instance ?: throw RuntimeException("ApiClient must be initialized.")
        }
    }

    private val okHttpClient: OkHttpClient =
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
                .addInterceptor(BasicAuthInterceptor(authParameters.login, authParameters.password))
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(authParameters.login, authParameters.password))
                .build()
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl(authParameters.server)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val client: ApiInterface = retrofit.create(ApiInterface::class.java)
}
