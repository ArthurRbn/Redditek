package com.example.redditek.redditApi

import android.service.autofill.UserData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header

interface UserDataService {
    @GET("/api/v1/me")
    abstract fun getUserData(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
    ): Call<UserDataResponse>
}

class UserDataResponse {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("icon_img")
    @Expose
    var urlProfilePic: String? = null

    @SerializedName("subreddit")
    @Expose
    var more_infos: UserDataSub? = null

    @SerializedName("awarder_karma")
    @Expose
    var awarder_karma: Int? = 0

    @SerializedName("awardee_karma")
    @Expose
    var awardee_karma: Int? = 0

    @SerializedName("link_karma")
    @Expose
    var publication_karma: Int? = 0

    @SerializedName("comment_karma")
    @Expose
    var comment_karma: Int? = 0

    @SerializedName("total_karma")
    @Expose
    var total_karma: Int? = 0

    @SerializedName("created_utc")
    @Expose
    var account_creation_date: Long? = 0
}

class UserDataSub {
    @SerializedName("subscribers")
    @Expose
    var subscribers: Int? = null

    @SerializedName("public_description")
    @Expose
    var description: String? = null
}