package com.mercandalli.server.files.log

import com.mercandalli.server.files.time.TimeManager
import io.ktor.request.*
import org.json.JSONArray
import kotlin.collections.ArrayList

internal class LogManagerImpl(
    rootPath: String,
    private val timeManager: TimeManager
) : LogManager {

    private val log1418File = java.io.File(rootPath, "static/1418/contact-us.json")
    private val contactUs1418List = ArrayList<ContactUs>()

    init {
        if (log1418File.exists()) {
            val json = log1418File.readText()
            val jsonArray = JSONArray(json)
            val contactUs1418 = ContactUs.fromJson(jsonArray)
            contactUs1418List.addAll(contactUs1418)
        } else {
            log1418File.createNewFile()
            saveContactUs1418()
        }
    }

    override fun d(tag: String, message: String) {
        val tags = listOf(tag)
        val contentString = HashMap<String, String>()
        contentString["message"] = message
        val log = LogData(
            LogData.Type.Description,
            tags,
            timeManager.getDayString(),
            timeManager.getTimeString(),
            timeManager.getTimeMillis(),
            contentString,
            HashMap(),
            HashMap()
        )
        print(log)
    }

    override fun e(tag: String, message: String) {
        val tags = listOf(tag)
        val contentString = HashMap<String, String>()
        contentString["message"] = message
        val log = LogData(
            LogData.Type.Error,
            tags,
            timeManager.getDayString(),
            timeManager.getTimeString(),
            timeManager.getTimeMillis(),
            contentString,
            HashMap(),
            HashMap()
        )
        print(log)
    }

    override fun logRequest(tag: String, request: ApplicationRequest) {
        val tags = listOf(tag, "Request")
        val uri = request.uri
        val local = request.local
        val remoteHost = local.remoteHost
        val method = local.method.value
        val userAgent = request.userAgent() ?: ""
        val contentString = HashMap<String, String>()
        contentString["uri"] = uri
        contentString["remote_host"] = remoteHost
        contentString["method"] = method
        contentString["user_agent"] = userAgent
        val log = LogData(
            LogData.Type.Description,
            tags,
            timeManager.getDayString(),
            timeManager.getTimeString(),
            timeManager.getTimeMillis(),
            contentString,
            HashMap(),
            HashMap()
        )
        print(log)
    }

    override fun logResponse(tag: String, request: ApplicationRequest, response: String) {
        val tags = listOf(tag, "Response")
        val uri = request.uri
        val contentString = HashMap<String, String>()
        contentString["uri"] = uri
        contentString["response"] = response
        val log = LogData(
            LogData.Type.Description,
            tags,
            timeManager.getDayString(),
            timeManager.getTimeString(),
            timeManager.getTimeMillis(),
            contentString,
            HashMap(),
            HashMap()
        )
        print(log)
    }

    override fun log1418ContactUs(
        firstName: String?,
        lastName: String?,
        email: String?,
        text: String?
    ) {
        val time = timeManager.getTimeString()
        val contactUs = ContactUs(
            time,
            firstName,
            lastName,
            email,
            text
        )
        contactUs1418List.add(contactUs)
        saveContactUs1418()
    }

    private fun saveContactUs1418() {
        val jsonArray = ContactUs.toJson(contactUs1418List)
        log1418File.writeText(jsonArray.toString())
    }

    private fun print(logData: LogData) {
        println(logData.toTerminalInput())
    }
}
