package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface UserSubscriptionsService {
    @GET("/subreddits/mine/subscriber")
    abstract fun getUserSubscriptions(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?
    ): Call<UserSubscriptionsResponse>
}

class UserSubscriptionsResponse {
    @SerializedName("data")
    @Expose
    var data: SubscriptionsInfo? = null
}

class SubscriptionsInfo {
    @SerializedName("children")
    @Expose
    var subreddits: List<SubredditInfosResponse>? = null
}