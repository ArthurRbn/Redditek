package com.example.redditek;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenService {
    @POST("/api/v1/access_token")
    @FormUrlEncoded
    Call<TokenResponse> getToken(@Header("Authorization") String Authorization, @Field("grant_type") String grant_type, @Field("code") String code, @Field("redirect_uri") String redirect_uri);
}
