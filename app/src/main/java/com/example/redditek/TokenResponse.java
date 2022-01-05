package com.example.redditek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("access_token")
    @Expose
    public String access_token;

    @SerializedName("token_type")
    @Expose
    public String token_type;

    @SerializedName("expires_in")
    @Expose
    public String expires_in;

    @SerializedName("scope")
    @Expose
    public String scope;

    @SerializedName("refresh_token")
    @Expose
    public String refresh_token;
}