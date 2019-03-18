package com.mercandalli.server.files.remote_config_ios

import org.json.JSONObject

object RemoteConfigIosYoutube {

    const val APP_BUNDLE_YOUTUBE = "com.mercandalli.ios.youtube3"

    fun createRemoteConfigIosYoutubeJson(
        appBundle: String,
        appVersionName: String,
        localCountryIso31662: String
    ): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("app_bundle", appBundle)
        jsonObject.put("app_version_name", appVersionName)
        jsonObject.put("video_player_background", true)
        jsonObject.put("subscription_full_version_sku", "appstore.$appBundle.subscription.1")
        return jsonObject
    }
}
