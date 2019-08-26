@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.event_android

import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.head
import kotlinx.html.title
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.body
import org.json.JSONObject

object EventAndroidHandler {

    suspend fun PipelineContext<Unit, ApplicationCall>.androidEventPost(
        appPackageName: String,
        appVersionName: String,
        idAddress: String,
        requestBody: String
    ) {
        val eventHandlerPost = ApplicationGraph.getEventHandlerPost()
        val eventResponse = eventHandlerPost.handleEvent(
            "android",
            appPackageName,
            appVersionName,
            idAddress,
            requestBody
        )
        val responseJsonObject = JSONObject()
        responseJsonObject.put("succeeded", eventResponse.isSucceeded())
        responseJsonObject.put("events_added_count", eventResponse.getEventsAddedCount())
        responseJsonObject.put("events_written_count", eventResponse.getEventsWrittenCount())
        call.respondText(
            responseJsonObject.toString()
        )
    }

    suspend fun PipelineContext<Unit, ApplicationCall>.androidEventGet(
        appPackageName: String,
        appVersionName: String,
        ipAddress: String
    ) {
        if (ipAddress != "192.168.1.254" && ipAddress != "127.0.0.1") {
            call.respondHtml {
                head {
                    title { +"Event dashboard" }
                }
                body {
                    h1 {
                        +"Event dashboard. App $appPackageName and version $appVersionName"
                    }
                    p {
                        +"Ip address $ipAddress"
                    }
                }
            }
            return
        }
        val eventHandlerGet = ApplicationGraph.getEventHandlerGet()
        eventHandlerGet.get(
            "android",
            appPackageName,
            appVersionName,
            call
        )
    }
}
