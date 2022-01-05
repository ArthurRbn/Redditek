package com.example.redditek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_notifications.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import android.R.attr.text

import android.R.attr

import android.widget.TextView
import com.example.redditek.redditApi.*
import kotlinx.android.synthetic.main.fragment_settings.*
import okhttp3.internal.notify


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch_nightmode.setOnClickListener{ switchNightModeSubreddit() }
        switch_over_18.setOnClickListener{ switchOver18() }
        switch_search_over_18.setOnClickListener{ switchSearchOver18() }
        switch_location_recommendations.setOnClickListener { switchLocationRecommendations() }
        switch_email_post_reply.setOnClickListener { switchEmailPostReply() }
        switch_email_notifications.setOnClickListener { switchEmailNotifications() }
        getUserSettings()
    }

    private fun getUserSettings() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://oauth.reddit.com/").build()
        val service = retrofit.create<UserSettingsService>()
        val call = service.getUserSettings("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)")

        call.enqueue(object : retrofit2.Callback<UserSettingsResponse> {
            override fun onResponse(call: Call<UserSettingsResponse>, response: Response<UserSettingsResponse>) {
                val parsing: UserSettingsResponse? = response.body()
                if (parsing != null) {
                    switch_nightmode.isChecked = parsing.nightmode
                    switch_over_18.isChecked = parsing.over_18
                    switch_search_over_18.isChecked = parsing.search_over_18
                    switch_location_recommendations.isChecked = parsing.location_recommendations
                    switch_email_post_reply.isChecked = parsing.email_post_reply
                    switch_email_notifications.isChecked = parsing.email_unsubscribe_all
                }
            }

            override fun onFailure(call: Call<UserSettingsResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchNightModeSubreddit() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchNightModeService>()
        val call = service.switchEmailMessages("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", NightModeParams(switch_nightmode.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchOver18() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchOver18Service>()
        val call = service.switchOver18("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", Over18Params(switch_over_18.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchSearchOver18() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchSearchOver18Service>()
        val call = service.switchSearchOver18("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", SearchOver18Params(switch_search_over_18.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchLocationRecommendations() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchLocationRecommendationsService>()
        val call = service.switchLocationRecommendations("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", SwitchRecommendationsParams(switch_location_recommendations.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchEmailNotifications() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchEmailNotificationsService>()
        val call = service.switchEmailNotifications("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", SwitchEmailNotifParams(switch_email_notifications.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun switchEmailPostReply() {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SwitchEmailPostReplyService>()
        val call = service.switchPostReplyNotifications("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", SwitchEmailPostReplyParams(switch_email_post_reply.isChecked))

        call.enqueue(object : retrofit2.Callback<BooleanSettingResponse> {
            override fun onResponse(call: Call<BooleanSettingResponse>, response: Response<BooleanSettingResponse>) {
            }

            override fun onFailure(call: Call<BooleanSettingResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

}