package com.example.redditek

import retrofit2.Call
import retrofit2.http.*

interface Token {
    interface TokenService {
        @POST("/access_token")
        @FormUrlEncoded
        fun getToken(
            @Header("Authorization") Authorization: String?,
            @Field("grant_type") grant_type: String?,
            @Field("code") code: String?,
            @Field("redirect_uri") redirect_uri: String?
        ): Call<TokenResponse?>?
    }
}