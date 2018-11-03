package com.mercandalli.server.files.log

import org.json.JSONArray
import org.json.JSONObject

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
