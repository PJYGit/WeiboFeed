package com.example.simplebooster.data

data class FeedMessage(
    val nickname: String, val photo: String, val isVip: Int, val time: Long, val from: String,
    val text: String, val hasImage: Int, val imageList: ArrayList<Image>,
    val hasVideo: Int, val videoList: ArrayList<Video>,
    val forward: Int, val comments: Int, val likes: Int
)

data class Image(val id: Int, val url: String)
data class Video(val id: Int, val url: String)