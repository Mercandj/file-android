package com.mercandalli.sdk.event.mercan

class EventModule(
    private val eventSecure: EventSecure
) {

    fun createEventSender(
        url: String,
        eventNetwork: EventNetwork
    ): EventSender {
        return EventSenderImpl(
            url,
            eventNetwork,
            eventSecure
        )
    }

    fun createEventReceiver(): EventReceiver {
        return EventReceiverImpl(
            eventSecure
        )
    }
}
