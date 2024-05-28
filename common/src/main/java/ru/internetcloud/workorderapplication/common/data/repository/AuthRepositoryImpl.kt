package ru.internetcloud.workorderapplication.common.data.repository

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import javax.inject.Inject
import retrofit2.HttpException
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.data.network.dto.AuthResponse
import ru.internetcloud.workorderapplication.common.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.common.domain.common.AuthResult
import ru.internetcloud.workorderapplication.common.domain.repository.AuthRepository
import ru.internetcloud.workorderapplication.common.domain.repository.AuthorizationPreferencesRepository

class AuthRepositoryImpl @Inject constructor(
    private var authParameters: AuthParameters,
    private val authorizationPreferencesRepository: AuthorizationPreferencesRepository
) : AuthRepository {

    override fun getAuthParameters(): AuthParameters {
        return authParameters
    }

    override fun setAuthParameters(server: String, login: String, password: String) {
        authParameters = AuthParameters(server, login, password)
    }

    override suspend fun checkAuthorization(): AuthResult {
        var authResult = AuthResult(false, "Нет связи с сервером.")

        // HTTP FAILED: java.net.UnknownHostException: Unable to resolve host "serv.promintel-agro.ru":
        // No address associated with hostname
        // инициализировать ApiClient
        try {
            ApiClient.initialize(authParameters)
        } catch (e: Exception) {
            authResult.isAuthorized = false
            authResult.errorMessage = "Нет связи с сервером."
            return authResult
        }

        // пойти в интернет и проверить авторизацию на сервере 1С.
        try {
            val authResponse: AuthResponse = ApiClient.getInstance().client.checkAuthorization()
            authResult.isAuthorized = authResponse.isAuthorized
        } catch (e: SocketTimeoutException) {
            authResult.isAuthorized = false
            authResult.errorMessage = "Не удалось подключиться к серверу 1С!\n\nНет связи с сервером.\n\n${e.message}"
        } catch (e: UnknownHostException) {
            authResult.isAuthorized = false
            authResult.errorMessage = "Не удалось подключиться к серверу 1С!\n\nВозможно, неправильное имя сервера!\n\n${e.message}"
        } catch (e: HttpException) {
            authResult.isAuthorized = false
            if (e.code() == HTTP_INTERNAL_SERVER_ERROR) {
                authResult.errorMessage = "Не удалось подключиться к серверу 1С!\n\nВнутренняя ошибка на сервере 1С!\n\n${e.message}"
            } else {
                authResult.errorMessage = "Не удалось подключиться к серверу 1С!\n\nВозможно, неправильный логин или пароль!\n\n${e.message}"
            }
        } catch (e: Exception) {
            authResult.isAuthorized = false
            authResult.errorMessage = "Не удалось подключиться к серверу 1С!\n\n${e.message}"
        }

        if (authResult.isAuthorized) {
            // если авторизация на сервере 1С прошла....
            savePasswordHashToPreferences()
        } else {
            // иначе авторизация на сервере 1С не прошла, но
            // тогда запасной вариант - локальная проверка авторизации
            authResult = checkLocalAuthorization(authResult)
        }

        return authResult
    }

    private fun savePasswordHashToPreferences() {
        val key = authParameters.server + authParameters.login
        val hash = sha1(authParameters.password)

        authorizationPreferencesRepository.setStoredPasswordHash(key, hash)
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

        val storedHash = authorizationPreferencesRepository.getStoredPasswordHash(key)

        if (storedHash.isNotEmpty()) {
            if (hash.equals(storedHash)) {
                authResult.isAuthorized = true
                authResult.errorMessage = DEFAULT_STRING_VALUE
            } else {
                // authResult.errorMessage = "Неправильный логин или пароль"
                // - уже есть сообщение о невозможности подключения от Интернет-попытки подключения!
            }
        }

        return authResult
    }

    companion object {
        private const val HTTP_INTERNAL_SERVER_ERROR = 500
        private const val DEFAULT_STRING_VALUE = ""
    }
}
