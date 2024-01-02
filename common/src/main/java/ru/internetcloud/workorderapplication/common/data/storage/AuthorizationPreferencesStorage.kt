package ru.internetcloud.workorderapplication.common.data.storage

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.FirstLaunchRepository
import ru.internetcloud.workorderapplication.common.domain.common.util.orDefault

class AuthorizationPreferencesStorage @Inject constructor(
    applicaton: Application,
    firstLaunchRepository: FirstLaunchRepository
) {
    private var authSharedPrefs: SharedPreferences =
        applicaton.getSharedPreferences(AUTH_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    init {
        if (firstLaunchRepository.isFirstLaunch()) {
            firstLaunchRepository.setFirstLaunchToFalse()

            val oldServer = getOldStoredServer(applicaton)
            if (oldServer.isNotEmpty()) {
                setStoredServer(oldServer)
            }

            val oldLogin = getOldStoredLogin(applicaton)
            if (oldLogin.isNotEmpty()) {
                setStoredLogin(oldLogin)
            }

            if (oldServer.isNotEmpty() and oldLogin.isNotEmpty()) {
                val oldKey = oldServer + oldLogin
                val oldHash = getOldStoredPasswordHash(applicaton, oldKey)
                setStoredPasswordHash(oldKey, oldHash)
            }
        }
    }

    fun setStoredServer(server: String) {
        authSharedPrefs.edit().putString(KEY_SERVER, server).apply()
    }

    fun getStoredServer(): String = authSharedPrefs.getString(KEY_SERVER, DEFAULT_VALUE).orDefault()

    fun setStoredLogin(login: String) {
        authSharedPrefs.edit().putString(KEY_LOGIN, login).apply()
    }

    fun getStoredLogin(): String = authSharedPrefs.getString(KEY_LOGIN, DEFAULT_VALUE).orDefault()

    fun setStoredPasswordHash(key: String, passwordHash: String) {
        authSharedPrefs.edit().putString(key, passwordHash).apply()
    }

    fun getStoredPasswordHash(key: String): String {
        return authSharedPrefs.getString(key, DEFAULT_VALUE).orDefault()
    }

    private fun getOldStoredServer(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(OLD_PREF_SERVER, DEFAULT_VALUE).orDefault()
    }

    private fun getOldStoredLogin(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(OLD_PREF_LOGIN, DEFAULT_VALUE).orDefault()
    }

    private fun getOldStoredPasswordHash(context: Context, key: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, DEFAULT_VALUE).orDefault()
    }

    companion object {
        private const val AUTH_SHARED_PREFS_NAME = "auth_shared_prefs"
        private const val KEY_SERVER = "key_server"
        private const val KEY_LOGIN = "key_login"

        private const val OLD_PREF_SERVER = "server"
        private const val OLD_PREF_LOGIN = "login"

        private const val DEFAULT_VALUE = ""
    }
}
