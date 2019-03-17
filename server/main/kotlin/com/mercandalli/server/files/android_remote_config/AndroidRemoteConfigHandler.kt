@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.android_remote_config

import com.mercandalli.server.files.android_remote_config.AndroidRemoteConfigSpeedometer.APP_PACKAGE_NAME_SPEEDOMETER
import com.mercandalli.server.files.android_remote_config.AndroidRemoteConfigSpeedometer.createRemoteConfigSpeedometerJson
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONObject

object AndroidRemoteConfigHandler {

    suspend fun PipelineContext<Unit, ApplicationCall>.androidRemoteConfigGet(
        appPackageName: String,
        appVersionName: String,
        localCountryIso31662: String
    ) {
        val jsonObject = createRemoteConfigJson(
            appPackageName,
            appVersionName,
            localCountryIso31662
        )
        call.respondText(
            jsonObject.toString()
        )
    }

    private fun createRemoteConfigJson(
        appPackageName: String,
        appVersionName: String,
        localCountryIso31662: String
    ): JSONObject {
        return when (appPackageName) {
            APP_PACKAGE_NAME_SPEEDOMETER -> createRemoteConfigSpeedometerJson(
                appPackageName,
                appVersionName,
                localCountryIso31662
            )
            else -> createRemoteConfigDefaultJson(
                appPackageName
            )
        }
    }

    private fun createRemoteConfigDefaultJson(
        appPackageName: String
    ): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("app_package_name", appPackageName)
        jsonObject.put("debug", "App not supported")
        return jsonObject
    }
}
