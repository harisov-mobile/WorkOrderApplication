package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import android.util.Log
import java.net.SocketTimeoutException
import java.security.MessageDigest
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.AuthResponse
import ru.internetcloud.workorderapplication.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.domain.common.AuthResult
import ru.internetcloud.workorderapplication.domain.common.AuthorizationPreferences
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository

class AuthRepositoryImpl private constructor(private val application: Application) : AuthRepository {

    private var authParameters: AuthParameters = AuthParameters()

    companion object {
        private const val DATABASE_NAME = "work_order.db"

        private var instance: AuthRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = AuthRepositoryImpl(application)
            }
        }

        fun get(): AuthRepositoryImpl {
            return instance ?: throw RuntimeException("LocalAuthRepositoryImpl must be initialized.")
        }
    }

    override fun getAuthParameters(): AuthParameters {
        return authParameters
    }

    override fun setAuthParameters(server: String, login: String, password: String) {

        authParameters = AuthParameters(server, login, password)
    }

    override suspend fun checkAuthorization(): AuthResult {

        var authResult = AuthResult(false, "Нет связи с сервером.")

        // инициализировать ApiClient
        try {
            ApiClient.initialize(authParameters)
        } catch (e: Exception) {
            Log.i("rustam", "Нет связи с сервером. 1й try: " + e.toString())
            authResult.isAuthorized = false
            authResult.errorMessage = "Нет связи с сервером."
            return authResult
        }

        Log.i("rustam", "Перед 2й try")

        // пойти в интернет и проверить авторизацию на сервере 1С.
        try {
            val authResponse: AuthResponse = ApiClient.getInstance().client.checkAuthorization()
            authResult.isAuthorized = authResponse.isAuthorized
            Log.i("rustam", " authResult.isAuthorized = authResponse.isAuthorized    ${authResult.isAuthorized}")
        } catch (e: SocketTimeoutException) {
            Log.i("rustam", "Нет связи с сервером. 2й try: " + e.toString())
            authResult.isAuthorized = false
            authResult.errorMessage = "Нет связи с сервером."
        } catch (e: Exception) {
            authResult.isAuthorized = false
            authResult.errorMessage = "Неправильный логин или пароль!"
            Log.i("rustam", "Неправильный логин или пароль! 3й try: " + e.toString())
        }

        if (authResult.isAuthorized) {
            // если авторизация на сервере 1С прошла....
            savePasswordHashToPreferences()
        } else {
            // иначе авторизация на сервере 1С не прошла, но
            // тогда запасной вариант - локальная проверка авторизации
            authResult = checkLocalAuthorization(authResult)
        }

        Log.i("rustam", "isAuthorized = ${authResult.isAuthorized}")
        return authResult
    }

    private fun savePasswordHashToPreferences() {

        val key = authParameters.server + authParameters.login
        val hash = sha1(authParameters.password)

        AuthorizationPreferences.setStoredPasswordHash(application, key, hash)
    }

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return printHexBinary(bytes).uppercase()
    }

    fun sha1(input: String) = hashString("SHA-1", input)

    fun printHexBinary(data: ByteArray): String {

        val HEX_CHARS = "0123456789ABCDEF".toCharArray()

        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }

    fun checkLocalAuthorization(authResult: AuthResult): AuthResult {
        val key = authParameters.server + authParameters.login
        val hash = sha1(authParameters.password)

        val storedHash = AuthorizationPreferences.getStoredPasswordHash(application, key)

        if (!storedHash.equals("")) {
            if (hash.equals(storedHash)) {
                authResult.isAuthorized = true
                authResult.errorMessage = ""
            } else {
                authResult.errorMessage = "Неправильный логин или пароль"
            }
        }

        return authResult
    }
}