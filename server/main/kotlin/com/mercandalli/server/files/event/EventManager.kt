package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event

interface EventManager {

    fun handle(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        idAddress: String,
        events: List<Event>
    ): EventResponse

    fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String
    ): List<String>
}
