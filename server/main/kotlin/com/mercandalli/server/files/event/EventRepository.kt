package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event

interface EventRepository {

    fun put(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        events: List<Event>
    ): EventResponse
}
