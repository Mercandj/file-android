package com.mercandalli.android.sdk.files.api.online

import com.mercandalli.android.sdk.files.api.online.utils.Base64
import com.mercandalli.android.sdk.files.api.online.utils.HashUtils
import java.text.SimpleDateFormat
import java.util.*

internal class FileOnlineLoginManagerImpl : FileOnlineLoginManager {

    private var login: String? = null
    private var password: String? = null

    override fun set(login: String, password: String) {
        this.login = login
        this.password = HashUtils.sha1(password)
    }

    override fun getLogin() = login

    override fun hasToken() = login != null && password != null

    override fun createToken() = createTokenInternal(login!!, password!!)

    companion object {

        private fun createTokenInternal(login: String, password: String): String {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            df.timeZone = TimeZone.getTimeZone("gmt")
            val date = Date()
            val currentDate = df.format(date)
            val passwordHash = HashUtils.sha1(password)
            val passwordHashWithDate = HashUtils.sha1(passwordHash + currentDate)
            val authenticationClear = String.format("%s:%s", login, passwordHashWithDate)
            return Base64.encodeBytes(authenticationClear.toByteArray())
        }
    }
}