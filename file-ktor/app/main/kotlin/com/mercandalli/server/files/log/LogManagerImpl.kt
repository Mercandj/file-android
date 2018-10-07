package com.mercandalli.server.files.log

import java.text.SimpleDateFormat
import java.util.*

internal class LogManagerImpl : LogManager {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    init {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
    }

    override fun d(tag: String, message: String) {
        val date = createDate()
        println("$date [$tag] $message")
    }

    private fun createDate() = simpleDateFormat.format(Date())
}