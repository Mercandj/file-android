package com.mercandalli.server.files.log

import io.ktor.request.ApplicationRequest
import io.ktor.request.uri
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

    override fun logRequest(tag: String, request: ApplicationRequest) {
        val uri = request.uri
        d(tag, "Request: $uri")
    }

    private fun createDate() = simpleDateFormat.format(Date())
}