package com.mercandalli.server.files.event

interface EventHandlerPost {

    fun handleEvent(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        body: String
    ): EventResponse
}
