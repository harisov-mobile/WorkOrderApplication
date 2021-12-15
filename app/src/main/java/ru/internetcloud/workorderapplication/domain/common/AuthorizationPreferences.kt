package ru.internetcloud.workorderapplication.domain.common

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_SERVER = "server"
private const val PREF_LOGIN = "login"

object AuthorizationPreferences {

    fun setStoredServer(context: Context, server: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SERVER, server)
            .apply()
    }

    fun getStoredServer(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SERVER, "")!!
    }

    fun setStoredLogin(context: Context, login: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_LOGIN, login)
            .apply()
    }

    fun getStoredLogin(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_LOGIN, "")!!
    }

    fun setStoredPasswordHash(context: Context, key: String, passwordHash: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(key, passwordHash)
            .apply()
    }

    fun getStoredPasswordHash(context: Context, key: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, "")!!
    }
}
