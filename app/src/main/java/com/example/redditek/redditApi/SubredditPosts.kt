package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface SubredditPostsService {
    @GET("r/{subreddit}/{sorting}/.json?limit=50")
    abstract fun getSubredditPosts(
        @Header("User-Agent") UserAgent: String?,
        @Path("subreddit") subreddit: String?,
        @Path("sorting") sorting: String?,
        @Query("after") after: String?
    ): Call<SubredditPostsResponse>
}

class SubredditPostsResponse {
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("data")
    @Expose
    var data: ListInfo? = null
}