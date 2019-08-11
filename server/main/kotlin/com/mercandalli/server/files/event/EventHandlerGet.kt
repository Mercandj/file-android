package com.mercandalli.server.files.event

interface EventHandlerGet {

    fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<String>
}
