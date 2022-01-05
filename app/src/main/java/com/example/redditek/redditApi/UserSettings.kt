package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UserSettingsService {
    @GET("/api/v1/me/prefs")
    abstract fun getUserSettings(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
    ): Call<UserSettingsResponse>
}

class UserSettingsResponse {
    @SerializedName("nightmode")
    @Expose
    var nightmode: Boolean = false

    @SerializedName("over_18")
    @Expose
    var over_18: Boolean = false

    @SerializedName("search_include_over_18")
    @Expose
    var search_over_18: Boolean = false

    @SerializedName("show_location_based_recommendations")
    @Expose
    var location_recommendations: Boolean = false

    @SerializedName("email_unsubscribe_all")
    @Expose
    var email_unsubscribe_all: Boolean = false

    @SerializedName("email_post_reply")
    @Expose
    var email_post_reply: Boolean = false
}