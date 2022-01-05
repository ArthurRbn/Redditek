package com.example.redditek.redditApi

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutService {
    @POST("/api/v1/revoke_token")
    @FormUrlEncoded
    abstract fun logout(@Header("Authorization") Authorization: String?,
                        @Header("User-Agent") UserAgent: String?,
                        @Field("token") token: String?,
                        @Field("token_type_hint") token_type_hint: String?
    ): Call<ResponseBody>
}

class LogoutResponse {

}