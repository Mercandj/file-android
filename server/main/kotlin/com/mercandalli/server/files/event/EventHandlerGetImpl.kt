package com.mercandalli.server.files.event

import com.mercandalli.sdk.event.mercan.Event
import com.mercandalli.server.files.log.LogManager
import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.title
import kotlinx.html.style
import kotlinx.html.p
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.table
import kotlinx.html.tr
import kotlinx.html.th
import kotlinx.html.a
import kotlinx.html.ul
import kotlinx.html.li
import java.text.SimpleDateFormat
import java.util.Locale

class EventHandlerGetImpl(
    private val eventManager: EventManager,
    private val eventRepository: EventRepository,
    private val logManager: LogManager
) : EventHandlerGet {

    private val eventMaxSize = 500
    private val eventTimeDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    override suspend fun get(
        platform: String,
        applicationPackageName: String,
        applicationVersionName: String,
        call: ApplicationCall
    ) {
        logManager.d("EventHandlerGet", "$platform $applicationPackageName $applicationVersionName")
        val lines = eventManager.get(
            platform,
            applicationPackageName,
            applicationVersionName
        )
        val events = getEventsToDisplay(applicationPackageName, applicationVersionName)
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
                    +"Event dashboard. App $applicationPackageName and version $applicationVersionName"
                }
                for (line in lines) {
                    p {
                        +line
                    }
                }
                h3 {
                    +"Events displayed: ${events.size} / ${eventMaxSize}"
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
        applicationPackageName: String,
        applicationVersionName: String
    ): List<Event> {
        val events = eventRepository.get("android", applicationPackageName, applicationVersionName)
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
