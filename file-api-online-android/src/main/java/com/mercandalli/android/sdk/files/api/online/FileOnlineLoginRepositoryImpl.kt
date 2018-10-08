package com.mercandalli.android.sdk.files.api.online

import android.content.SharedPreferences
import com.mercandalli.sdk.files.api.online.FileOnlineLoginRepository

internal class FileOnlineLoginRepositoryImpl(
        private val sharedPreferences: SharedPreferences
) : FileOnlineLoginRepository {

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun load(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "FileOnlineLoginRepository"
    }
}