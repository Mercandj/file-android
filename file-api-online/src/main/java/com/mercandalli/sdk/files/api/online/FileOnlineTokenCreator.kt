package com.mercandalli.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.utils.HashUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object FileOnlineTokenCreator {

    fun createToken(login: String, passwordSha1: String): String {
        val date = Date()
        return createToken(login, passwordSha1, date)
    }

    fun createTokens(login: String, passwordSha1: String, minutesRange: Int): List<String> {
        val tokens = ArrayList<String>()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        for (i in -minutesRange / 2..minutesRange / 2) {
            calendar.set(Calendar.HOUR_OF_DAY, hour + i)
            val token = createToken(login, passwordSha1, calendar.time)
            tokens.add(token)
        }
        return tokens
    }

    private fun createToken(login: String, passwordSha1: String, date: Date): String {
        return createToken(login, passwordSha1, date.time)
    }

    private fun createToken(login: String, passwordSha1: String, date: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH", Locale.US)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
        val currentDate = simpleDateFormat.format(date)
        val passwordHash = HashUtils.sha1(passwordSha1)
        val passwordHashWithDate = HashUtils.sha1(passwordHash + currentDate)
        val authenticationClear = String.format("%s:%s", login, passwordHashWithDate)
        return com.mercandalli.sdk.files.api.online.utils.Base64.encodeBytes(authenticationClear.toByteArray())
    }
}