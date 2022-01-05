package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface SubredditInfosService {
    @GET("/r/{query}/about.json")
    abstract fun searchSubreddit(
        @Header("User-Agent") UserAgent: String?,
        @Path("query") query: String?
    ): Call<SubredditInfosResponse>
}

class SubredditInfosResponse {
    @SerializedName("data")
    @Expose
    var subreddits: SubredditDatas? = null
}

class SubredditDatas {
    @SerializedName("display_name")
    @Expose
    var subName: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("banner_background_image")
    @Expose
    var header_img: String? = null

    @SerializedName("icon_img")
    @Expose
    var icon_img: String? = null

    @SerializedName("public_description")
    @Expose
    var description: String? = null

    @SerializedName("subscribers")
    @Expose
    var subscribers: Int? = null
}