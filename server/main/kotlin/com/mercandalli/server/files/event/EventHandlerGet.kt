package com.mercandalli.server.files.event

import io.ktor.application.ApplicationCall

interface EventHandlerGet {

    suspend fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        call: ApplicationCall
    )
}
