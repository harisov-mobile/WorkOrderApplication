package ru.internetcloud.workorderapplication.domain.repository

interface AuthorizationPreferencesRepository {

    fun setStoredServer(server: String)

    fun getStoredServer(): String

    fun setStoredLogin(login: String)

    fun getStoredLogin(): String

    fun setStoredPasswordHash(key: String, passwordHash: String)

    fun getStoredPasswordHash(key: String): String
}
