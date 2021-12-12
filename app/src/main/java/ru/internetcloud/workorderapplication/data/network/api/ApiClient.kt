package ru.internetcloud.workorderapplication.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.internetcloud.workorderapplication.BuildConfig

object ApiClient {

    private var client: OkHttpClient =
        if (BuildConfig.DEBUG) {
            //val user = "test1"
            val user = "1стест"
            val pwd = "588977"
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
                .addInterceptor(BasicAuthInterceptor(user, pwd))
                .build()
        } else {
            //val user = "test1"
            val user = "1стест"
            val pwd = "588977"
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(user, pwd))
                .build()
        }

    val apiClient: ApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return@lazy retrofit.create(ApiInterface::class.java)
    }

}
