package com.example.redditek

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.internal.format
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthenticationActivity: AppCompatActivity() {
    private val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
            "&response_type=code&state=%s&redirect_uri=%s&" +
            "duration=permanent&scope=identity account edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"

    private val CLIENT_ID = "1eROjts-CqM4dRZu1pwfJA"

    private val REDIRECT_URI = "YOUR_REDIRECT_URI"

    private val STATE = "YOUR_RANDOM_STRING"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val rollButton: Button = findViewById(R.id.signIn)
        rollButton.setOnClickListener { startSignIn() }
    }

    fun startSignIn() {
        val url: String = format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        catchToken(intent)
    }

    fun catchToken(intent: Intent): Boolean {
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri!!.getQueryParameter("error") != null) {
                val error = uri.getQueryParameter("error")
                Log.e(ContentValues.TAG, "An error has occurred : $error")
            } else {
                val state = uri.getQueryParameter("state")
                if (state == STATE) {
                    val code = uri.getQueryParameter("code")
                    if (code != null) {
                        getAccessToken(code)
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun getAccessToken(code: String): Boolean {
        val retrofit = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(TokenService::class.java)
        val authString = "$CLIENT_ID:$AppSecret"
        val encodedAuthString: String = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
        val call = service.getToken("Basic $encodedAuthString", "authorization_code", code, REDIRECT_URI)

        call.enqueue(object : retrofit2.Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                val parsing: TokenResponse? = response.body()
                val accessToken = parsing?.access_token

                if (accessToken != null) {
                    nextActivity(accessToken)
                } else {
                    println("No access token")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
        return true
    }

    private fun nextActivity(accessToken: String) {
        println("token: $accessToken")
        access_token = accessToken
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        var access_token = ""
        var BaseUrl = "https://www.reddit.com/"
        var AppSecret = "YOUR_CLIENT_SECRET"
    }
}