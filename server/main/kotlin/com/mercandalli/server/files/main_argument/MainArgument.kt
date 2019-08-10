package com.mercandalli.server.files.main_argument

import org.json.JSONObject
import java.io.File

class MainArgument(
    private val fileOnlineAuthenticationLogin: String,
    private val fileOnlineAuthenticationPasswordSha1: String,
    private val eventSecureKey: String,
    private val eventSecureIv: String
) {

    fun getFileOnlineAuthenticationLogin() = fileOnlineAuthenticationLogin

    fun getFileOnlineAuthenticationPasswordSha1() = fileOnlineAuthenticationPasswordSha1

    fun getEventSecureKey() = eventSecureKey

    fun getEventSecureIv() = eventSecureIv

    companion object {

        fun fromJsonFile(file: File): MainArgument {
            val jsonObject = JSONObject(file.readText())
            return fromJson(jsonObject)
        }

        fun fromJson(jsonObject: JSONObject): MainArgument {
            val fileOnlineAuthenticationLogin = jsonObject.getString(
                "file_online_authentication_login"
            )
            val fileOnlineAuthenticationPasswordSha1 = jsonObject.getString(
                "file_online_authentication_password_sha1"
            )
            val eventSecureKey = jsonObject.getString(
                "event_secure_key"
            )
            val eventSecureIv = jsonObject.getString(
                "event_secure_iv"
            )
            return MainArgument(
                fileOnlineAuthenticationLogin,
                fileOnlineAuthenticationPasswordSha1,
                eventSecureKey,
                eventSecureIv
            )
        }
    }
}
