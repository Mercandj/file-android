package com.mercandalli.sdk.event.mercan

interface EventReceiver {

    fun receive(requestBody: String): List<Event>
}
