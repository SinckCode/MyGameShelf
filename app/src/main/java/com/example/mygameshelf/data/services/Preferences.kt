package com.example.mygameshelf.data.services

import com.russhwolf.settings.Settings

object Preferences {

    private val settings = Settings()

    private const val KEY_IS_LOGGED = "isLogged"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USER_NAME = "userName"

    fun saveUserId(userId: String?) {
        if (userId != null)
            settings.putString(KEY_USER_ID, userId)
    }

    fun saveUserName(name: String?) {
        if (!name.isNullOrBlank()) {
            settings.putString(KEY_USER_NAME, name)
        }
    }

    fun saveIsLogged(isLogged: Boolean) {
        settings.putBoolean(KEY_IS_LOGGED, isLogged)
    }

    fun getIsLogged(): Boolean =
        settings.getBoolean(KEY_IS_LOGGED, false)

    fun getUserId(): String =
        settings.getString(KEY_USER_ID, "")

    fun getUserName(): String =
        settings.getString(KEY_USER_NAME, "")

    fun clearSettings() {
        settings.clear()
    }
}
