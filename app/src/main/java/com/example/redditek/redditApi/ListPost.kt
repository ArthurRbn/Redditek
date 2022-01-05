package com.example.redditek.redditApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface ListPostService {
    @GET("/{sorting}/.json?limit=25")
    abstract fun getListPost(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") UserAgent: String?,
        @Path("sorting") sorting: String?,
        @Query("after") after: String?
    ): Call<ListPostResponse>
}

class ListPostResponse {
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("data")
    @Expose
    var data: ListInfo? = null
}

class ListInfo {
    @SerializedName("after")
    @Expose
    var after: String? = null

    @SerializedName("dist")
    @Expose
    var size: String? = null

    @SerializedName("modhash")
    @Expose
    var modhash: String? = null

    @SerializedName("geo_filter")
    @Expose
    var geo_filter: String? = null

    @SerializedName("children")
    @Expose
    var posts: List<Posts>? = null
}

class Posts {
    @SerializedName("kind")
    @Expose
    var kind: String? = null

    @SerializedName("data")
    @Expose
    var postData: PostData? = null
}

class PostData {
    @SerializedName("approved_at_utc")
    @Expose
    var kind: String? = null

    @SerializedName("subreddit")
    @Expose
    var subreddit: String? = null

    @SerializedName("selftext")
    @Expose
    var description: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("post_hint")
    @Expose
    var post_hint: String? = null

    @SerializedName("domain")
    @Expose
    var domain: String? = null

    @SerializedName("is_video")
    @Expose
    var is_video: Boolean? = null

    @SerializedName("url_overridden_by_dest")
    @Expose
    var simple_medial_url: String? = null

    @SerializedName("secure_media")
    @Expose
    var secure_media: SecureMedia? = null
}

class SecureMedia {
    @SerializedName("reddit_video")
    @Expose
    var reddit_video: RedditVideo? = null

    @SerializedName("oembed")
    @Expose
    var oembed: Oembed? = null
}

class Oembed {
    @SerializedName("author_url")
    @Expose
    var author_url: String? = null

    @SerializedName("thumbnail_url")
    @Expose
    var thumbnail_url: String? = null
}

class RedditVideo {
    @SerializedName("fallback_url")
    @Expose
    var complex_media_url: String? = null
}