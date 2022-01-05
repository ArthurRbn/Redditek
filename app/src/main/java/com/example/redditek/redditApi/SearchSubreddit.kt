package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface SearchSubredditService {
    @GET("/api/search_reddit_names")
    abstract fun searchSubreddit(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Query("query") query: String?
    ): Call<SearchSubredditResponse>
}

class SearchSubredditResponse {
    @SerializedName("names")
    @Expose
    var subreddits: List<String>? = null
}
