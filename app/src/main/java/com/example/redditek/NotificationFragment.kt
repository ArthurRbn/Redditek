package com.example.redditek

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redditek.redditApi.UserDataResponse
import com.example.redditek.redditApi.UserDataService
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
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue


class NotificationFragment : Fragment(R.layout.fragment_notifications) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserInfos()

    }

    private fun getUserInfos(sorting: String = "best") {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retrofit.create<UserDataService>()
        val call = service.getUserData("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)")
        call.enqueue(object : retrofit2.Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                val parsing: UserDataResponse? = response.body()
                val questionMark = parsing?.urlProfilePic?.indexOf('?', 0)
                parsing?.urlProfilePic = questionMark?.let {
                    parsing?.urlProfilePic?.substring(0,
                        it
                    )
                }
                Picasso.get().load(parsing?.urlProfilePic).into(img_profile)
                name_txt.text = parsing?.name
                value_karma_pub_txt.text = parsing?.publication_karma.toString()
                value_karma_com_txt.text = parsing?.comment_karma.toString()
                value_karma_phil_txt.text = parsing?.awarder_karma.toString()
                value_karma_recip_txt.text = parsing?.awardee_karma.toString()
                val totalKarma = "${parsing?.total_karma} karma -"
                karma_txt.text = totalKarma
                val followers = "${parsing?.more_infos?.subscribers} followers"
                followers_txt.text = followers

                if (parsing != null) {
                    val stamp = Timestamp(parsing.account_creation_date!!)
                    date_creation_txt.text = "${Date(stamp.time * 1000).toString()} -"

                    val currentTimestamp = System.currentTimeMillis()
                    val diff = currentTimestamp - stamp.time * 1000
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    val years = days / 365

                    val age = "${years}y ${days.mod(365)}d -"
                    birthday_txt.text = age
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }
}