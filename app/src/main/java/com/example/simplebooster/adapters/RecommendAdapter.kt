package com.example.simplebooster.adapters

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ctetin.expandabletextviewlibrary.ExpandableTextView
import com.example.simplebooster.R
import com.example.simplebooster.activities.EmptyActivity
import com.example.simplebooster.data.FeedMessage
import com.example.simplebooster.data.MyMedia
import com.example.simplebooster.data.NineGridItem
import com.example.simplebooster.layouts.NineGridViewGroup


class RecommendAdapter(val msgList: ArrayList<FeedMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.photo)
        val nickname: TextView = view.findViewById(R.id.nickname)
        val time: TextView = view.findViewById(R.id.time)
        val vip: ImageView = view.findViewById(R.id.vip)
        val text: ExpandableTextView = view.findViewById(R.id.text)
        val forward: TextView = view.findViewById(R.id.forward)
        val comment: TextView = view.findViewById(R.id.comment)
        val likes: TextView = view.findViewById(R.id.likes)
        val delete: ImageView = view.findViewById(R.id.delete)

        val nine: NineGridViewGroup = view.findViewById(R.id.nineGrid)
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.photo)
        val nickname: TextView = view.findViewById(R.id.nickname)
        val time: TextView = view.findViewById(R.id.time)
        val vip: ImageView = view.findViewById(R.id.vip)
        val text: ExpandableTextView = view.findViewById(R.id.text)
        val forward: TextView = view.findViewById(R.id.forward)
        val comment: TextView = view.findViewById(R.id.comment)
        val likes: TextView = view.findViewById(R.id.likes)
        val pause: ImageView = view.findViewById(R.id.pause)
        val cover: ImageView = view.findViewById(R.id.cover)
        val delete: ImageView = view.findViewById(R.id.delete)

        val video: VideoView = view.findViewById(R.id.video)
    }

    // TODO: 待抽离BaseHolder，两个holder有很多可复用代码

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
            return ImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video, parent, false)
            return VideoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feedMsg = msgList[position]
        when (holder) {
            is ImageViewHolder -> {
                holder.nickname.text = feedMsg.nickname
                holder.time.text = feedMsg.time.toString()
                holder.text.setContent(feedMsg.text)
                Glide.with(holder.photo.context)
                    .load(feedMsg.photo)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(holder.photo)
                if (feedMsg.isVip <= 0) {
                    holder.vip.visibility = View.INVISIBLE
                }
                holder.forward.text = feedMsg.forward.toString()
                holder.comment.text = feedMsg.comments.toString()
                holder.likes.text = feedMsg.likes.toString()

                holder.delete.setOnClickListener {
                    msgList.removeAt(position)
                    notifyItemRemoved(position)
                }

                // 构建九宫格数据和九宫格自定义布局的Adapter
                val mediaList = ArrayList<MyMedia>()
                for (image in feedMsg.imageList) {
                    mediaList.add(MyMedia(image.url))
                }
                if (mediaList != null && mediaList.size > 0) {
                    val nineItemList = ArrayList<NineGridItem>()
                    for (mymedia in mediaList) {
                        nineItemList.add(
                            NineGridItem(
                                mymedia.imageUrl,
                                mymedia.imageUrl,
                                mymedia.videoUrl
                            )
                        )
                    }
                    // 设置适配器
                    holder.nine.setAdapter(NineGridViewAdapter(nineItemList))
                } // end settings of ImageViewHolder
            } // end ImageViewHolder
            is VideoViewHolder -> {
                holder.nickname.text = feedMsg.nickname
                holder.time.text = feedMsg.time.toString()
                holder.text.setContent(feedMsg.text)
                Glide.with(holder.photo.context)
                    .load(feedMsg.photo)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(holder.photo)
                if (feedMsg.isVip <= 0) {
                    holder.vip.visibility = View.INVISIBLE
                }
                holder.forward.text = feedMsg.forward.toString()
                holder.comment.text = feedMsg.comments.toString()
                holder.likes.text = feedMsg.likes.toString()

                holder.delete.setOnClickListener {
                    msgList.removeAt(position)
                    notifyItemRemoved(position)
                }

                // 设置VideoView TODO: 抽离函数

                // Set up the media controller widget and attach it to the video view.
//                val controller = MediaController(holder.video.context, false)
//                controller.setMediaPlayer(holder.video)
//                holder.video.setMediaController(controller)


                // Buffer and decode the video
                val videoUri = getMedia(feedMsg.videoList[0].url)
                holder.video.setVideoURI(videoUri)

                // Listener for onPrepared() event (runs after the media is prepared).
                holder.pause.bringToFront()
                Glide.with(holder.cover.context)
                    .setDefaultRequestOptions(RequestOptions().frame(0))
                    .load(feedMsg.videoList[0].url)
                    .into(holder.cover)

                holder.video.setOnPreparedListener(
                    OnPreparedListener {
                        // pause
                        // holder.cover.setImageBitmap(mask!!)
                        holder.cover.visibility = View.INVISIBLE
                        holder.pause.visibility = View.INVISIBLE
                        holder.video.start()
                    })

                holder.pause.setOnClickListener {
                    it.visibility = View.INVISIBLE
                    holder.cover.visibility = View.INVISIBLE
                    holder.video.start()
                }

                holder.video.setOnClickListener {
                    val intent = Intent(it.context, EmptyActivity::class.java)
                    it.context.startActivity(intent)
                }

                // Listener for onCompletion() event (runs after media has finished
                // playing).
                holder.video.setOnCompletionListener(
                    OnCompletionListener {
                        // Return the video position to the start.
                        holder.video.seekTo(0)
                        holder.cover.visibility = View.VISIBLE
                        holder.pause.visibility = View.VISIBLE
                        holder.pause.bringToFront()
                    })
            }
        }

    }

    private fun getMedia(mediaName: String): Uri {
        return Uri.parse(mediaName)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        return msgList[position].hasVideo
    }
}