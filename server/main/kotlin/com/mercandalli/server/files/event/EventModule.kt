package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.EventReceiver
import com.mercandalli.sdk.event.mercan.EventSecure
import com.mercandalli.sdk.feature_aes.AesMode
import com.mercandalli.sdk.feature_aes.AesOpMode
import com.mercandalli.sdk.feature_aes.AesPadding
import com.mercandalli.server.files.main.ApplicationGraph
import java.io.File

class EventModule {

    private val eventManager by lazy { createEventManager() }

    fun createEventHandlerGet(): EventHandlerGet {
        val eventRepository = ApplicationGraph.getEventRepository()
        val logManager = ApplicationGraph.getLogManager()
        return EventHandlerGetImpl(
            eventManager,
            eventRepository,
            logManager
        )
    }

    fun createEventHandlerPost(): EventHandlerPost {
        val eventReceiver = createEventReceiver()
        val logManager = ApplicationGraph.getLogManager()
        return EventHandlerPostImpl(
            eventManager,
            eventReceiver,
            logManager
        )
    }

    fun createEventRepository(): EventRepository {
        val timeManager = ApplicationGraph.getTimeManager()
        val rootPath = ApplicationGraph.getRootPath()
        return EventRepositoryImpl(
            timeManager,
            File(rootPath)
        )
    }

    private fun createEventManager(): EventManager {
        val eventRepository = ApplicationGraph.getEventRepository()
        val timeManager = ApplicationGraph.getTimeManager()
        return EventManagerImpl(
            eventRepository,
            timeManager
        )
    }

    private fun createEventReceiver(): EventReceiver {
        val eventSecure = createEventSecure()
        return com.mercandalli.sdk.event.mercan.EventModule(
            eventSecure
        ).createEventReceiver()
    }

    private fun createEventSecure(): EventSecure {
        val aesBase64Manager = ApplicationGraph.getAesBase64Manager()
        val mainArgument = ApplicationGraph.getMainArgument()
        return object : EventSecure {
            override fun crypt(message: String): String {
                return aesBase64Manager.getAesCrypter(
                    AesOpMode.CRYPT,
                    AesMode.GCM,
                    AesPadding.NO,
                    mainArgument.getEventSecureKey(),
                    mainArgument.getEventSecureIv()
                ).crypt(message)
            }

            override fun decrypt(message: String): String {
                return aesBase64Manager.getAesCrypter(
                    AesOpMode.DECRYPT,
                    AesMode.GCM,
                    AesPadding.NO,
                    mainArgument.getEventSecureKey(),
                    mainArgument.getEventSecureIv()
                ).decrypt(message)
            }
        }
    }
}
