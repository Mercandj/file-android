package com.mercandalli.server.files.main_argument

import org.json.JSONObject
import java.io.File

class MainArgument(
    private val fileOnlineAuthenticationLogin: String,
    private val fileOnlineAuthenticationPasswordSha1: String,
    private val eventSecureKey: String,
    private val eventSecureIv: String,
    private val adminIpAddresses: Set<String>
) {

    fun getFileOnlineAuthenticationLogin() = fileOnlineAuthenticationLogin

    fun getFileOnlineAuthenticationPasswordSha1() = fileOnlineAuthenticationPasswordSha1

    fun getEventSecureKey() = eventSecureKey

    fun getEventSecureIv() = eventSecureIv

    fun getAdminIpAddresses() = HashSet(adminIpAddresses)

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
            val adminIpAddressesJsonArray = jsonObject.getJSONArray(
                "admin_ip_addresses"
            )
            val adminIpAddresses = HashSet<String>()
            for (i in 0 until adminIpAddressesJsonArray.length()) {
                adminIpAddresses.add(adminIpAddressesJsonArray.getString(i))
            }
            return MainArgument(
                fileOnlineAuthenticationLogin,
                fileOnlineAuthenticationPasswordSha1,
                eventSecureKey,
                eventSecureIv,
                adminIpAddresses
            )
        }
    }
}
