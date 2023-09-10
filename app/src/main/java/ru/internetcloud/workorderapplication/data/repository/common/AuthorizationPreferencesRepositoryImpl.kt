package ru.internetcloud.workorderapplication.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.storage.AuthorizationPreferencesStorage
import ru.internetcloud.workorderapplication.domain.repository.AuthorizationPreferencesRepository

class AuthorizationPreferencesRepositoryImpl @Inject constructor(
    private val authorizationPreferencesStorage: AuthorizationPreferencesStorage
) : AuthorizationPreferencesRepository {

    override fun setStoredServer(server: String) {
        authorizationPreferencesStorage.setStoredServer(server)
    }

    override fun getStoredServer(): String {
        return authorizationPreferencesStorage.getStoredServer()
    }

    override fun setStoredLogin(login: String) {
        authorizationPreferencesStorage.setStoredLogin(login)
    }

    override fun getStoredLogin(): String {
        return authorizationPreferencesStorage.getStoredLogin()
    }

    override fun setStoredPasswordHash(key: String, passwordHash: String) {
        authorizationPreferencesStorage.setStoredPasswordHash(key, passwordHash)
    }

    override fun getStoredPasswordHash(key: String): String {
        return authorizationPreferencesStorage.getStoredPasswordHash(key)
    }
}
