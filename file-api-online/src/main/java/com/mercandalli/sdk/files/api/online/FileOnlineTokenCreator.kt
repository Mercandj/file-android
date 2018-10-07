package com.mercandalli.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.utils.HashUtils
import java.text.SimpleDateFormat
import java.util.*

object FileOnlineTokenCreator {

    fun createToken(login: String, passwordSha1: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
        val date = Date()
        val currentDate = simpleDateFormat.format(date)
        val passwordHash = HashUtils.sha1(passwordSha1)
        val passwordHashWithDate = HashUtils.sha1(passwordHash + currentDate)
        val authenticationClear = String.format("%s:%s", login, passwordHashWithDate)
        return com.mercandalli.sdk.files.api.online.utils.Base64.encodeBytes(authenticationClear.toByteArray())
    }
}