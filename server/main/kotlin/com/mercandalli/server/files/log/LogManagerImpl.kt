package com.mercandalli.server.files.log

import com.mercandalli.server.files.time.TimeManager
import io.ktor.request.ApplicationRequest
import io.ktor.request.uri
import org.json.JSONArray
import org.json.JSONObject
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
        val time = timeManager.getTimeString()
        println("$ANSI_CYAN$time$ANSI_RESET [$tag] $message")
    }

    override fun e(tag: String, message: String) {
        val time = timeManager.getTimeString()
        println("$ANSI_RED_BOLD$time [$tag] $message$ANSI_RESET")
    }

    override fun logRequest(tag: String, request: ApplicationRequest) {
        val uri = request.uri
        d("$tag][Request", "Uri: $uri")
    }

    override fun logResponse(tag: String, request: ApplicationRequest, response: String) {
        val uri = request.uri
        d("$tag][Response", "Uri: $uri\nResponse: $response")
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

    companion object {

        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_BLACK = "\u001B[30m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_GREEN = "\u001B[32m"
        private const val ANSI_YELLOW = "\u001B[33m"
        private const val ANSI_BLUE = "\u001B[34m"
        private const val ANSI_PURPLE = "\u001B[35m"
        private const val ANSI_CYAN = "\u001B[36m"
        private const val ANSI_WHITE = "\u001B[37m"
        private const val ANSI_RED_BOLD = "\u001B[1;31m"

    }

    data class ContactUs(
            val time: String,
            val firstName: String?,
            val lastName: String?,
            val email: String?,
            val text: String?
    ) {
        companion object {

            fun fromJson(jsonObject: JSONObject): ContactUs {
                val time = jsonObject.getString("time")
                val firstName = jsonObject.getString("first_name")
                val lastName = jsonObject.getString("last_name")
                val email = jsonObject.getString("email")
                val text = jsonObject.getString("text")
                return ContactUs(
                        time,
                        firstName,
                        lastName,
                        email,
                        text
                )
            }

            fun fromJson(jsonArray: JSONArray): List<ContactUs> {
                val list = ArrayList<ContactUs>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val contactUs = fromJson(jsonObject)
                    list.add(contactUs)
                }
                return list
            }

            fun toJson(contactUs: ContactUs): JSONObject {
                val jsonObject = JSONObject()
                jsonObject.put("time", contactUs.time)
                jsonObject.put("first_name", contactUs.firstName)
                jsonObject.put("last_name", contactUs.lastName)
                jsonObject.put("email", contactUs.email)
                jsonObject.put("text", contactUs.text)
                return jsonObject
            }

            fun toJson(contactUss: List<ContactUs>): JSONArray {
                val jsonArray = JSONArray()
                for (contactUs in contactUss) {
                    val jsonObject = toJson(contactUs)
                    jsonArray.put(jsonObject)
                }
                return jsonArray
            }
        }
    }
}