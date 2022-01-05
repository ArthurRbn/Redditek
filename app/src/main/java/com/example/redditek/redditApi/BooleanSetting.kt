package com.example.redditek.redditApi

import androidx.core.view.accessibility.AccessibilityEventCompat
import retrofit2.Call
import retrofit2.http.*

class NightModeParams(value: Boolean) {
    val email_messages = value
}

interface SwitchNightModeService {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchEmailMessages(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() email_messages: NightModeParams
    ): Call<BooleanSettingResponse>
}

class Over18Params(value: Boolean) {
    val over_18 = value
}

interface SwitchOver18Service {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchOver18(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() over_18: Over18Params
    ): Call<BooleanSettingResponse>
}

class SearchOver18Params(value: Boolean) {
    val search_include_over_18 = value
}

interface SwitchSearchOver18Service {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchSearchOver18(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() search_include_over_18: SearchOver18Params
    ): Call<BooleanSettingResponse>
}

class SwitchRecommendationsParams(value: Boolean) {
    val show_location_based_recommendations = value
}

interface SwitchLocationRecommendationsService {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchLocationRecommendations(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() show_location_based_recommendations: SwitchRecommendationsParams
    ): Call<BooleanSettingResponse>
}

class SwitchEmailNotifParams(value: Boolean) {
    val email_unsubscribe_all = value
}

interface SwitchEmailNotificationsService {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchEmailNotifications(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() email_unsubscribe_all: SwitchEmailNotifParams
    ): Call<BooleanSettingResponse>
}

class SwitchEmailPostReplyParams(value: Boolean) {
    val email_post_reply = value
}

interface SwitchEmailPostReplyService {
    @PATCH("/api/v1/me/prefs?raw_json=1&gilding_detail=1")
    abstract fun switchPostReplyNotifications(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Body() email_post_reply: SwitchEmailPostReplyParams
    ): Call<BooleanSettingResponse>
}

class BooleanSettingResponse {

}

