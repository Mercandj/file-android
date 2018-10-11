package com.mercandalli.server.files.time

import java.text.SimpleDateFormat
import java.util.*

internal class TimeManagerImpl : TimeManager {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    init {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
    }

    override fun getTimeString() = simpleDateFormat.format(Date())!!
}