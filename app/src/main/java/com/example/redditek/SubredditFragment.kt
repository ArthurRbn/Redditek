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
import android.graphics.Color
import android.util.Base64
import android.widget.Button

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redditek.redditApi.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_subreddit.*


class SubredditFragment : Fragment(R.layout.fragment_subreddit) {
    private var subsList = mutableListOf<String>()
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<String>()
    private var pagination: String = ""
    public var fSubList = mutableListOf<String>()
    public var subName = ""
    var isSub = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val index = args?.getString("sub", "")
        subName = index?.drop(2).toString()
        if (subName != null) {
            getSubredditInfos(subName)
            getUserSubscriptions()
        }
        val button_follow = activity?.findViewById(R.id.button_follow_sub) as Button
        button_follow.setOnClickListener() {
            if (!isSub) {
                joinSub(subName)
                isSub = true
            } else if (isSub) {
                leaveSub(subName)
                isSub = false
            }
        }
    }

    private fun joinSub(subreddit: String = "") {
        if (subreddit == "")
            return
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://oauth.reddit.com/").build()
        val service = retrofit.create<JoinSubredditService>()
        val call = service.joinSubreddit("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", "sub", subreddit)

        call.enqueue(object : retrofit2.Callback<JoinSubredditResponse> {
            override fun onResponse(call: Call<JoinSubredditResponse>, response: Response<JoinSubredditResponse>) {
                button_follow_sub.text = "Unfollow"
                button_follow_sub.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_minus_24, 0, 0, 0)
            }

            override fun onFailure(call: Call<JoinSubredditResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun leaveSub(subreddit: String = "") {
        if (subreddit == "")
            return
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://oauth.reddit.com/").build()
        val service = retrofit.create<JoinSubredditService>()
        val call = service.joinSubreddit("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", "unsub", subreddit)

        call.enqueue(object : retrofit2.Callback<JoinSubredditResponse> {
            override fun onResponse(call: Call<JoinSubredditResponse>, response: Response<JoinSubredditResponse>) {
                button_follow_sub.text = "Follow"
                button_follow_sub.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0)
            }

            override fun onFailure(call: Call<JoinSubredditResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun applyR() {
        val btn_best = activity?.findViewById(R.id.button_best_sub) as Button
        val btn_hot = activity?.findViewById(R.id.button_hot_sub) as Button
        val btn_top = activity?.findViewById(R.id.button_top_sub) as Button
        sub_recycleView.apply {
            getSubredditPosts(title_sub.text as String, "best", pagination)

            sub_recycleView.layoutManager = LinearLayoutManager(activity)
            sub_recycleView.adapter = RecyclerAdapter(subsList, titlesList, descList, imageList)
        }
        btn_best.setOnClickListener() {
            pagination = ""
            btn_best.setBackgroundResource(R.drawable.transparent_button)
            btn_hot.setBackgroundResource(R.drawable.round_button)
            btn_top.setBackgroundResource(R.drawable.round_button)
            btn_hot.setTextColor(Color.WHITE)
            btn_top.setTextColor(Color.WHITE)
            btn_best.setTextColor(Color.BLUE)
            sub_recycleView.apply {
                getSubredditPosts(title_sub.text as String, "best", pagination)
            }
        }
        btn_hot.setOnClickListener() {
            pagination = ""
            btn_hot.setBackgroundResource(R.drawable.transparent_button)
            btn_best.setBackgroundResource(R.drawable.round_button)
            btn_top.setBackgroundResource(R.drawable.round_button)
            btn_best.setTextColor(Color.WHITE)
            btn_top.setTextColor(Color.WHITE)
            btn_hot.setTextColor(Color.BLUE)
            sub_recycleView.apply {
                getSubredditPosts(title_sub.text as String, "hot", pagination)
            }
        }
        btn_top.setOnClickListener() {
            pagination = ""
            btn_top.setBackgroundResource(R.drawable.transparent_button)
            btn_hot.setBackgroundResource(R.drawable.round_button)
            btn_best.setBackgroundResource(R.drawable.round_button)
            btn_hot.setTextColor(Color.WHITE)
            btn_best.setTextColor(Color.WHITE)
            btn_top.setTextColor(Color.BLUE)
            sub_recycleView.apply {
                getSubredditPosts(title_sub.text as String, "top", pagination)

            }
        }
    }

    private fun emptyList_sub() {
        subsList.clear()
        titlesList.clear()
        descList.clear()
        imageList.clear()
    }

    private fun getUserSubscriptions() {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://oauth.reddit.com/").build()
        val service = retrofit.create<UserSubscriptionsService>()
        val call = service.getUserSubscriptions("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)")

        call.enqueue(object : retrofit2.Callback<UserSubscriptionsResponse> {
            override fun onResponse(call: Call<UserSubscriptionsResponse>, response: Response<UserSubscriptionsResponse>) {
                val parsing: UserSubscriptionsResponse? = response.body()
                if (parsing != null) {
                    for (i in parsing?.data?.subreddits!!) {
                        //i.subreddits?.subName?.let { fSubList.add(it) }
                        fSubList.add(i.subreddits?.subName.toString())
                    }
                    for (j in fSubList) {
                        if (j.equals(subName)) {
                            button_follow_sub.text = "Unfollow"
                            button_follow_sub.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_minus_24, 0, 0, 0)
                            isSub = true
                        }
                    }
                    if (!isSub) {
                        button_follow_sub.text = "Follow"
                        button_follow_sub.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0)
                    }
                }
            }

            override fun onFailure(call: Call<UserSubscriptionsResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun getSubredditInfos(subreddit: String = "") {
        if (subreddit == "")
            return
        val retrofit = Retrofit.Builder().baseUrl("https://www.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SubredditInfosService>()
        val call = service.searchSubreddit("android:com.example.redditek:v1.0 (by /u/RedditechTester)", subreddit)

        call.enqueue(object : retrofit2.Callback<SubredditInfosResponse> {
            override fun onResponse(call: Call<SubredditInfosResponse>, response: Response<SubredditInfosResponse>) {
                val parsing: SubredditInfosResponse? = response.body()
                if (parsing != null){
                    if (parsing.subreddits?.header_img?.isEmpty() == false && parsing.subreddits?.icon_img?.isEmpty() == false){
                        val questionMark = parsing.subreddits?.header_img?.indexOf('?', 0)
                        parsing.subreddits?.header_img = questionMark?.let {
                            parsing.subreddits?.header_img?.substring(
                                0,
                                it
                            )
                        }
                        Picasso.get().load(parsing.subreddits?.header_img).fit()
                            .into(header_img_sub)
                        Picasso.get().load(parsing.subreddits?.icon_img).into(sub_img)
                    } else if (parsing.subreddits?.header_img?.isEmpty() == true && parsing.subreddits?.icon_img?.isEmpty() == false)
                        Picasso.get().load(parsing.subreddits?.icon_img).into(sub_img)
                    else if (parsing.subreddits?.header_img?.isEmpty() == false && parsing.subreddits?.icon_img?.isEmpty() == true) {
                        val questionMark = parsing.subreddits?.header_img?.indexOf('?', 0)
                        parsing.subreddits?.header_img = questionMark?.let {
                            parsing.subreddits?.header_img?.substring(
                                0,
                                it
                            )
                        }
                        Picasso.get().load(parsing?.subreddits?.header_img).fit()
                            .into(header_img_sub)
                    }
                    val textView = title_sub as TextView
                    textView.setText(parsing?.subreddits?.subName)
                    val descView = description_sub as TextView
                    descView.setText(parsing?.subreddits?.description)
                    val followView = nb_follow_sub as TextView
                    followView.setText(parsing?.subreddits?.subscribers.toString())
                    applyR()
                }
            }

            override fun onFailure(call: Call<SubredditInfosResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }
    private fun addToList(subreddit: String, title: String, description: String, image: String) {
        subsList.add(subreddit)
        titlesList.add(title)
        descList.add(description)
        imageList.add(image)
    }

    private fun getSubredditPosts(subreddit: String = "", sorting: String = "best", after: String = "") {
        if (subreddit == "")
            return
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://www.reddit.com/").build()
        val service = retrofit.create<SubredditPostsService>()
        val call = service.getSubredditPosts("android:com.example.redditek:v1.0 (by /u/RedditechTester)", subreddit, sorting, after)

        call.enqueue(object : retrofit2.Callback<SubredditPostsResponse> {
            override fun onResponse(call: Call<SubredditPostsResponse>, response: Response<SubredditPostsResponse>) {
                val parsing: SubredditPostsResponse? = response.body()
                val data = parsing?.data
                val posts: List<Posts>
                var check : Boolean = false
                if (data != null) {
                    pagination = data.after.toString()
                    posts = data.posts!!
                    val unsafeSize = data.size?.toInt()
                    val size = unsafeSize?.minus(1) ?: return
                    emptyList_sub()
                    for (i in 0..size) {
                        check = false
                        if (posts[i].postData?.simple_medial_url != null) {
                            val match = arrayOf("imgur", ".gif", "giphy", "gfycat").filter { posts[i].postData?.simple_medial_url!!.contains(it, true) }
                            if (match.contains("gfycat")) {
                                println("gif url = ${posts[i].postData?.secure_media?.oembed?.thumbnail_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.oembed?.thumbnail_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                                check = true
                            } else if (match.contains("imgur") && match.contains(".gif")) {
                                println("gif url = ${posts[i].postData?.secure_media?.oembed?.thumbnail_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.oembed?.thumbnail_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2.dropLast(1)
                                    )
                                } } }
                                check = true
                            } else if (match.isEmpty().not()) {
                                println("gif url = ${posts[i].postData?.simple_medial_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.simple_medial_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                                check = true
                            } else if (posts[i].postData?.post_hint == "image") {
                                println("image url = ${posts[i].postData?.simple_medial_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.simple_medial_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                                check = true
                            }
                        }
                        if (posts[i].postData?.secure_media != null) {
                            if (posts[i].postData?.post_hint == "rich:video" && posts[i].postData?.secure_media?.oembed != null) {
                                println("External video url = ${posts[i].postData?.secure_media?.oembed?.author_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.oembed?.author_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                                check = true
                            } else if (posts[i].postData?.post_hint == "hosted:video" && posts[i].postData?.secure_media?.reddit_video != null) {
                                println("Reddit-Hosted video url = ${posts[i].postData?.secure_media?.reddit_video?.complex_media_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.reddit_video?.complex_media_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2.dropLast(16)
                                    )
                                } } }
                                check = true
                            }
                        }
                        if (check == false) {
                            posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> addToList("r/${posts[i].postData?.subreddit}", it, it1, "") } }
                        }
                    }
                    sub_recycleView.layoutManager = LinearLayoutManager(activity)
                    sub_recycleView.adapter = RecyclerAdapter(subsList, titlesList, descList, imageList)
                }
            }

            override fun onFailure(call: Call<SubredditPostsResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun searchData(query: String) {
        val searchQuery = "%$query%"
    }
}