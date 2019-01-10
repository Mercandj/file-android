package com.mercandalli.sdk.files.api.online

import com.mercandalli.sdk.files.api.online.utils.HashUtils
import com.mercandalli.sdk.files.api.online.utils.encodeBase64ToString
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.io.ByteArrayOutputStream

class FileOnlineTokenCreatorImpl(
    val addOn: AddOn
) : FileOnlineTokenCreator {

    override fun createToken(login: String, passwordSha1: String): String {
        val date = addOn.getCurrentDate()
        return createToken(login, passwordSha1, date)
    }

    override fun createTokens(login: String, passwordSha1: String, minutesRange: Int): List<String> {
        val tokens = ArrayList<String>()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val minute = calendar.get(Calendar.MINUTE)
        for (i in -minutesRange / 2..minutesRange / 2) {
            calendar.set(Calendar.MINUTE, minute + i)
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
        val authenticationClear = String.format("%s:%s", login.toLowerCase(), passwordHashWithDate)
        return authenticationClear.encodeBase64ToString()
    }

    interface AddOn {

        fun getCurrentDate(): Date
    }
}
