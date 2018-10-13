package com.mercandalli.server.files.log

import io.ktor.request.ApplicationRequest

interface LogManager {

    fun d(tag: String, message: String)

    fun e(tag: String, message: String)

    fun logRequest(tag: String, request: ApplicationRequest)

    fun logResponse(tag: String, request: ApplicationRequest, response: String)

    fun log1418ContactUs(
            firstName: String?,
            lastName: String?,
            email: String?,
            text: String?
    )
}