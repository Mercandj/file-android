package com.mercandalli.server.files.log

import io.ktor.request.ApplicationRequest
import io.ktor.request.uri
import io.ktor.response.ApplicationResponse
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

internal class LogManagerImpl(
        rootPath: String
) : LogManager {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    private val log1418File = java.io.File(rootPath, "static/1418/contact-us.json")
    private val contactUs1418List = ArrayList<ContactUs>()

    init {
        simpleDateFormat.timeZone = TimeZone.getTimeZone("gmt")
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
        val date = createDate()
        println("$date [$tag] $message")
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
        val contactUs = ContactUs(
                firstName,
                lastName,
                email,
                text
        )
        contactUs1418List.add(contactUs)
        saveContactUs1418()
    }

    private fun createDate() = simpleDateFormat.format(Date())

    private fun saveContactUs1418() {
        val jsonArray = ContactUs.toJson(contactUs1418List)
        log1418File.writeText(jsonArray.toString())
    }

    data class ContactUs(
            val firstName: String?,
            val lastName: String?,
            val email: String?,
            val text: String?
    ) {
        companion object {

            fun fromJson(jsonObject: JSONObject): ContactUs {
                val firstName = jsonObject.getString("first_name")
                val lastName = jsonObject.getString("last_name")
                val email = jsonObject.getString("email")
                val text = jsonObject.getString("text")
                return ContactUs(
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