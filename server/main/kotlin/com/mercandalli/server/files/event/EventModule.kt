package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.EventReceiver
import com.mercandalli.sdk.event.mercan.EventSecure
import com.mercandalli.server.files.main.ApplicationGraph
import java.io.File

class EventModule {

    fun createEventHandlerPost(): EventHandlerPost {
        val eventManager = createEventManager()
        val eventReceiver = createEventReceiver()
        val logManager = ApplicationGraph.getLogManager()
        return EventHandlerPostImpl(
            eventManager,
            eventReceiver,
            logManager
        )
    }

    private fun createEventManager(): EventManager {
        val eventRepository = createEventRepository()
        return EventManagerImpl(
            eventRepository
        )
    }

    private fun createEventReceiver(): EventReceiver {
        val eventSecure = createEventSecure()
        return com.mercandalli.sdk.event.mercan.EventModule(
            eventSecure
        ).createEventReceiver()
    }

    private fun createEventSecure(): EventSecure {
        return object : EventSecure {
            override fun crypt(message: String): String {
                return message
            }

            override fun decrypt(message: String): String {
                return message
            }
        }
    }

    private fun createEventRepository(): EventRepository {
        val timeManager = ApplicationGraph.getTimeManager()
        val rootPath = ApplicationGraph.getRootPath()
        return EventRepositoryImpl(
            timeManager,
            File(rootPath)
        )
    }
}
