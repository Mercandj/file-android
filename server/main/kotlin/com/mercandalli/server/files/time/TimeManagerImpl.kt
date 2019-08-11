package com.mercandalli.server.files.time

import java.text.SimpleDateFormat
import java.util.*

internal class TimeManagerImpl : TimeManager {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val fileNameDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
    private val daySimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    init {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
    }

    override fun getDayString() = daySimpleDateFormat.format(Date())!!

    override fun getTimeString() = simpleDateFormat.format(Date())!!

    override fun getTimeFileNameString() = fileNameDateFormat.format(Date())!!.toLowerCase()

    override fun getTimeMillis() = System.currentTimeMillis()
}
