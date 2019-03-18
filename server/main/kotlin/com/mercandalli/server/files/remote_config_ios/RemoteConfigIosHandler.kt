@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.server.files.remote_config_ios

import com.mercandalli.server.files.remote_config_ios.RemoteConfigIosYoutube.APP_BUNDLE_YOUTUBE
import com.mercandalli.server.files.remote_config_ios.RemoteConfigIosYoutube.createRemoteConfigIosYoutubeJson
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONObject

object RemoteConfigIosHandler {

    suspend fun PipelineContext<Unit, ApplicationCall>.iosRemoteConfigGet(
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
        appBundle: String,
        appVersionName: String,
        localCountryIso31662: String
    ): JSONObject {
        return when (appBundle) {
            APP_BUNDLE_YOUTUBE -> createRemoteConfigIosYoutubeJson(
                appBundle,
                appVersionName,
                localCountryIso31662
            )
            else -> createRemoteConfigDefaultJson(
                appBundle
            )
        }
    }

    private fun createRemoteConfigDefaultJson(
        appBundle: String
    ): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("app_bundle", appBundle)
        jsonObject.put("debug", "App not supported")
        return jsonObject
    }
}
