package com.example.redditek

import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.view.MenuItemCompat

import android.view.MenuItem

import android.R.menu
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.SearchView
import com.example.redditek.redditApi.*

import retrofit2.create
import android.util.Log

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.Color.red
import android.os.Build

import android.view.MenuInflater

import android.view.Menu
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_subreddit.*


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var subsList = mutableListOf<String>()
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<String>()
    private var pagination: String = ""
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        setHasOptionsMenu(true);
        val btn_best = activity?.findViewById(R.id.button_best) as Button
        val btn_hot = activity?.findViewById(R.id.button_hot) as Button
        val btn_top = activity?.findViewById(R.id.button_top) as Button
        rv_recycleView.apply {
            if (pagination == "")
                postToList("best")
            else
                postToList("best", pagination)
        }
        btn_best.setOnClickListener() {
            pagination = ""
            btn_best.setBackgroundResource(R.drawable.transparent_button)
            btn_hot.setBackgroundResource(R.drawable.round_button)
            btn_top.setBackgroundResource(R.drawable.round_button)
            btn_hot.setTextColor(Color.WHITE)
            btn_top.setTextColor(Color.WHITE)
            btn_best.setTextColor(Color.BLUE)
            postToList("best")
            rv_recycleView.apply {
                if (pagination == "")
                    postToList("best")
                else
                    postToList("best", pagination)
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
            rv_recycleView.apply {
                if (pagination == "")
                    postToList("hot")
                else
                    postToList("hot", pagination)
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
            rv_recycleView.apply {
                if (pagination == "")
                    postToList("top")
                else
                    postToList("top", pagination)
            }
        }
    }

    private fun emptyList_sub() {
        subsList.clear()
        titlesList.clear()
        descList.clear()
        imageList.clear()
    }

    private fun searchSubreddit(query: String = "") {
        if (query == "")
            return
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<SearchSubredditService>()
        val call = service.searchSubreddit("Bearer ${MainActivity.AccessToken}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", query)

        call.enqueue(object : retrofit2.Callback<SearchSubredditResponse> {
            override fun onResponse(call: Call<SearchSubredditResponse>, response: Response<SearchSubredditResponse>) {
                val parsing: SearchSubredditResponse? = response.body()
                println("zebi : $query")
                if (parsing != null && parsing?.subreddits != null) {
                    emptyList_sub()
                    for (i in 0..parsing?.subreddits?.size!! - 1) {
                        addToList("r/${parsing?.subreddits!![i]}", "", "", "")
                    }
                    rv_recycleView.layoutManager = LinearLayoutManager(activity)
                    rv_recycleView.adapter = RecyclerAdapter(subsList, titlesList, descList, imageList)
                }
            }

            override fun onFailure(call: Call<SearchSubredditResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.menu_search_ic)
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i("onQueryTextChange", newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    rv_recycleView.apply { searchSubreddit(query) }
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_ic -> return false // Not implemented here
            else -> {
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

    private fun addToList(subreddit: String, title: String, description: String, image: String) {
        subsList.add(subreddit)
        titlesList.add(title)
        descList.add(description)
        imageList.add(image)
    }

    private fun emptyList() {
        subsList.clear()
        titlesList.clear()
        descList.clear()
        imageList.clear()
    }

    private fun postToList(sorting: String = "best", after: String = "null") {
        val retrofit = Retrofit.Builder().baseUrl("https://oauth.reddit.com/").addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<ListPostService>()
        val call = service.getListPost("Bearer ${AuthenticationActivity.access_token}", "android:com.example.redditek:v1.0 (by /u/RedditechTester)", sorting, after)

        call.enqueue(object : retrofit2.Callback<ListPostResponse> {
            override fun onResponse(call: Call<ListPostResponse>, response: Response<ListPostResponse>) {
                val parsing: ListPostResponse? = response.body()
                val data = parsing?.data
                val posts: List<Posts>
                var check : Boolean = false
                if (data != null) {
                    pagination = data.after.toString()
                    posts = data.posts!!
                    val unsafeSize = data.size?.toInt()
                    val size = unsafeSize?.minus(1) ?: return
                    emptyList()
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
                                posts[i].postData?.title?.let { it -> posts[i].postData?.description?.let { it1 -> posts[i].postData?.simple_medial_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                                check = true
                            } else if (posts[i].postData?.post_hint == "image") {
                                println("image url = ${posts[i].postData?.simple_medial_url}")
                                posts[i].postData?.title?.let { it -> posts[i].postData?.description?.let { it1 -> posts[i].postData?.simple_medial_url?.let { it2 ->
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
                                check = true
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.oembed?.author_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2
                                    )
                                } } }
                            } else if (posts[i].postData?.post_hint == "hosted:video" && posts[i].postData?.secure_media?.reddit_video != null) {
                                println("Reddit-Hosted video url = ${posts[i].postData?.secure_media?.reddit_video?.complex_media_url}")
                                check = true
                                println("Reddit-Hosted video url = ${posts[i].postData?.secure_media?.reddit_video?.complex_media_url}")
                                posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> posts[i].postData?.secure_media?.reddit_video?.complex_media_url?.let { it2 ->
                                    addToList("r/${posts[i].postData?.subreddit}", it, it1,
                                        it2.dropLast(16)
                                    )
                                } } }
                            }
                        }
                        if (check == false) {
                            posts[i].postData?.title?.let { posts[i].postData?.description?.let { it1 -> addToList("r/${posts[i].postData?.subreddit}", it, it1, "") } }
                        }
                    }
                    rv_recycleView.layoutManager = LinearLayoutManager(activity)
                    rv_recycleView.adapter = RecyclerAdapter(subsList, titlesList, descList, imageList)
                }
            }
            override fun onFailure(call: Call<ListPostResponse>, t: Throwable) {
                println("error")
                t.printStackTrace()
            }
        })
    }

    private fun searchData(query: String) {
        val searchQuery = "%$query%"
    }
}