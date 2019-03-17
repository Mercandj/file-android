package com.mercandalli.server.files.android_remote_config

import org.json.JSONObject

object AndroidRemoteConfigSpeedometer {

    const val APP_PACKAGE_NAME_SPEEDOMETER = "com.mercandalli.android.apps.speedometer"

    fun createRemoteConfigSpeedometerJson(
        appPackageName: String,
        appVersionName: String,
        localCountryIso31662: String
    ): JSONObject {
        val speedUnit = when (localCountryIso31662) {
            "US" -> "mph"
            else -> "hph"
        }
        val jsonObject = JSONObject()
        jsonObject.put("app_package_name", appPackageName)
        jsonObject.put("app_version_name", appVersionName)
        jsonObject.put("device_local_country_iso_31662", localCountryIso31662)
        jsonObject.put("default_speed_unit", speedUnit)
        jsonObject.put("force_update", false)
        jsonObject.put("force_update_message", "")
        jsonObject.put("message", "")
        jsonObject.put("on_boarding_enable", true)
        jsonObject.put("subscription_full_version_sku", "googleplay.$appPackageName.subscription.1")
        return jsonObject
    }
}
