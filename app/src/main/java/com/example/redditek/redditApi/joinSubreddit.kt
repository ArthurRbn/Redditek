package com.example.redditek.redditApi

import android.graphics.Paint
import retrofit2.Call
import retrofit2.http.*

interface JoinSubredditService {
    @POST("/api/subscribe")
    @FormUrlEncoded
    abstract fun joinSubreddit(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Field("action") action: String?,
        @Field("sr_name") sr_name: String?
    ): Call<JoinSubredditResponse>
}

class JoinSubredditResponse {
}