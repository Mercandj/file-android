@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.event_android

import com.mercandalli.sdk.event.mercan.Event
import com.mercandalli.server.files.main.ApplicationGraph
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object EventAndroidHandler {

    private const val eventMaxSize = 500
    private val eventTimeDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

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
        val lines = eventHandlerGet.get(
            "android",
            appPackageName,
            appVersionName
        )
        val events = getEventsToDisplay(appPackageName, appVersionName)
        call.respondHtml {
            head {
                title { +"Event dashboard" }
            }
            body {
                style {
                    +"""
                    table {
                        font: 1em Arial;
                        border: 1px solid black;
                        width: 100%;
                    }
                    th {
                        width: 200px;
                        font-weight: normal;
                    }
                    td {
                    }
                    th, td {
                        text-align: left;
                        padding: 0.4em 0.4em;
                    }
                """.trimIndent()
                }
                h1 {
                    +"Event dashboard. App $appPackageName and version $appVersionName"
                }
                p {
                    +"Ip address $ipAddress"
                }
                for (line in lines) {
                    p {
                        +line
                    }
                }
                h3 {
                    +"Events displayed: ${events.size} / $eventMaxSize"
                }
                table {
                    style = "font-size: 0.8em;"
                    tr {
                        th {
                            +"Time"
                        }
                        th {
                            +"KEY"
                        }
                        th {
                            style = "width: 280px;"
                            +"BOOLEANS"
                        }
                        th {
                            style = "width: 280px;"
                            +"LONGS"
                        }
                        th {
                            style = "width: 380px;"
                            +"STRINGS"
                        }
                    }
                    for (event in events) {
                        tr {
                            th {
                                +eventTimeDateFormat.format(event.getDeviceCurrentTimeMillis())
                            }
                            th {
                                +event.getKey()
                            }
                            th {
                                a {
                                    ul {
                                        style = "padding-inline-start: 0px;" +
                                            "    margin-block-start: 0em;" +
                                            "    margin-block-end: 0em;"
                                        for ((key, value) in event.getMetadataBoolean()) {
                                            li {
                                                +"$key $value"
                                            }
                                        }
                                    }
                                }
                            }
                            th {
                                a {
                                    ul {
                                        style = "padding-inline-start: 0px;" +
                                            "    margin-block-start: 0em;" +
                                            "    margin-block-end: 0em;"
                                        for ((key, value) in event.getMetadataLong()) {
                                            li {
                                                +"$key $value"
                                            }
                                        }
                                    }
                                }
                            }
                            th {
                                a {
                                    ul {
                                        style = "padding-inline-start: 0px;" +
                                            "    margin-block-start: 0em;" +
                                            "    margin-block-end: 0em;"
                                        for ((key, value) in event.getMetadataString()) {
                                            li {
                                                +"$key $value"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getEventsToDisplay(
        appPackageName: String,
        appVersionName: String
    ): List<Event> {
        val eventRepository = ApplicationGraph.getEventRepository()
        val events = eventRepository.get("android", appPackageName, appVersionName)
            .sortedBy {
                it.getDeviceCurrentTimeMillis()
            }
            .reversed()
        if (events.size <= eventMaxSize) {
            return events
        }
        return events.subList(0, eventMaxSize)
    }

    private fun Event.getDeviceCurrentTimeMillis(): Long {
        return getMetadataLong()["device_current_time_millis"]
            ?: error("Cannot find device_current_time_millis")
    }
}
