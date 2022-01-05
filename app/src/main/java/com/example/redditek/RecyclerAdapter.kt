package com.example.redditek

import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_subreddit.*

class RecyclerAdapter(private var subs: List<String>, private var titles : List<String>, private var descriptions : List<String>, private var images : List<String>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var mediaController: MediaController? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val postSubreddit : TextView = itemView.findViewById(R.id.post_subreddit)
            val postTitle : TextView = itemView.findViewById(R.id.post_title)
            val postDescription : TextView = itemView.findViewById(R.id.post_description)
            val itemPicture : ImageView = itemView.findViewById(R.id.iv_image)
            val itemVideo : VideoView = itemView.findViewById(R.id.iv_video)
            val itemText : TextView = itemView.findViewById(R.id.iv_hyperlink)

            init {
                itemView.setOnClickListener { v : View ->
                    val position : Int = adapterPosition
                    //Toast.makeText(itemView.context, "You clicked on item # ${position + 1}", Toast.LENGTH_SHORT).show()
                    val activity = v.context as AppCompatActivity
                    val redirectFragment = newInstance(position)
                    activity.supportFragmentManager.beginTransaction().replace(R.id.flFragment, redirectFragment).commit()
                }
            }
        }

    fun newInstance(position: Int): SubredditFragment {
        val f = SubredditFragment()
        val args = Bundle()
        args.putString("sub", subs[position])
        f.arguments = args
        return f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postSubreddit.text = subs[position]
        holder.postTitle.text = titles[position]
        holder.postDescription.text = descriptions[position]
        val match = arrayOf("imgur", ".gif", "giphy", "gfycat").filter { images[position].contains(it, true) }
        val matchVid = arrayOf(".mp4").filter { images[position].contains(it, true) }
        val matchVidYt = arrayOf("youtube").filter { images[position].contains(it, true) }
        if (images[position].isEmpty() == false && match.isEmpty() && matchVid.isEmpty() && matchVidYt.isEmpty()) {
            Glide.with(holder.itemPicture.context).load(images[position]).into(holder.itemPicture)
            holder.itemVideo.isVisible = false
        } else if (!matchVidYt.isEmpty()) {
            println(images[position])
            val hyperlinkView = holder.itemText
            hyperlinkView.setClickable(true)
            hyperlinkView.setMovementMethod(LinkMovementMethod.getInstance())
            hyperlinkView.setText(Html.fromHtml(images[position]))
            holder.itemVideo.isVisible = false
        } else if (!matchVid.isEmpty()) {
            if (mediaController == null) {
                mediaController = MediaController(holder.itemVideo.context)
                mediaController!!.setAnchorView(holder.itemVideo)
            }
            holder.itemVideo.setVideoURI(Uri.parse(images[position]))
            holder.itemVideo.seekTo( 1);
            holder.itemVideo.setMediaController(mediaController)
            holder.itemVideo.requestFocus()
            holder.itemVideo.start()
        } else {
            Glide.with(holder.itemPicture.context).asGif().load(images[position]).into(holder.itemPicture)
            holder.itemVideo.isVisible = false
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}