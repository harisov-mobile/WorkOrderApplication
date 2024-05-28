package ru.internetcloud.workorderapplication.common.data.network.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.internetcloud.workorderapplication.common.BuildConfig
import ru.internetcloud.workorderapplication.common.domain.common.AuthParameters

class ApiClient private constructor(private val authParameters: AuthParameters) {

    private val okHttpClient: OkHttpClient =
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                // .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
                .addInterceptor(BasicAuthInterceptor(authParameters.login, authParameters.password))
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(authParameters.login, authParameters.password))
                .build()
        }

    // необходимо эту переменную поместить ниже, чем okHttpClient
    // т.к. сначала должна пройти инициализация переменной okHttpClient
    val client: ApiInterface = getRetrofit().create(ApiInterface::class.java)

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(authParameters.server)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit
    }

    companion object {
        private var instance: ApiClient? = null

        fun initialize(authParameters: AuthParameters) {
            // каждый раз надо новый экземпляр класса создавать со своими новыми параметрами
            instance = ApiClient(authParameters)
        }

        fun getInstance(): ApiClient {
            return instance ?: error("ApiClient must be initialized.")
        }
    }
}
