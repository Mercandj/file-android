package com.mercandalli.sdk.event.mercan

import org.junit.Assert
import org.junit.Test
import java.io.File

class EventReceiverImplTest {

    @Test
    fun receive() {
        // Given
        val eventsRequestJsonFile = getEventsRequestJsonFile()
        val json = eventsRequestJsonFile.readText()
        val eventSecure = object : EventSecure {
            override fun crypt(message: String): String {
                return message
            }

            override fun decrypt(message: String): String {
                return message
            }
        }
        val eventReceiver = EventReceiverImpl(eventSecure)

        // When
        val events = eventReceiver.receive(json)

        // Then
        Assert.assertEquals(1, events.size)
    }

    private fun getEventsRequestJsonFile(): File {
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource("events_request.json")
        val resourcePath = resource.path
        return File(resourcePath)
    }
}
