package com.example.redditek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.example.redditek.redditApi.LogoutResponse
import com.example.redditek.redditApi.LogoutService
import com.example.redditek.redditApi.UserDataResponse
import com.example.redditek.redditApi.UserDataService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.nav_header.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import android.content.Intent
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {
    lateinit var toogle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toogle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val homeFragment = HomeFragment()
        val notificationsFragment = NotificationFragment()
        val settingsFragment = SettingsFragment()
        val subredditFragment = SubredditFragment()
        setCurrentFragment(homeFragment)
        getPictureNavBar()
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> setCurrentFragment(homeFragment)
                R.id.item2 -> setCurrentFragment(notificationsFragment)
                R.id.item3 -> setCurrentFragment(settingsFragment)
                R.id.item4 -> logout()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    private fun getPictureNavBar(sorting: String = "best") {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retrofit.create<UserDataService>()
        val call = service.getUserData("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)")
        call.enqueue(object : retrofit2.Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                val parsing: UserDataResponse? = response.body()
                val questionMark = parsing?.urlProfilePic?.indexOf('?', 0)
                parsing?.urlProfilePic = questionMark?.let {
                    parsing.urlProfilePic?.substring(0,
                        it
                    )
                }
                Picasso.get().load(parsing?.urlProfilePic).into(user_img_nav)
                val textView = nav_drawer_name as TextView
                textView.setText(parsing?.name)
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun logout() {
        val retrofit = Retrofit.Builder().baseUrl("https://www.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<LogoutService>()
        val CLIENT_ID = "YOUR_CLIENT_ID"
        val CLIENT_SECRET = "YOUR_CLIENT_SECRET"
        val authString = "$CLIENT_ID:$CLIENT_SECRET"
        val encodedAuthString: String = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)
        val call = service.logout("Basic $encodedAuthString","android:com.example.redditek:v1.0 (by /u/RedditechTester)", MainActivity.AccessToken, "access_token")

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("logout successfully")
                backToAuthentication()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("error logout")
                t.printStackTrace()
            }
        })
    }

    private fun backToAuthentication() {
        val i = Intent(this, AuthenticationActivity::class.java)
        startActivity(i)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        var AccessToken = AuthenticationActivity.access_token
    }
}