package com.example.simplebooster.network

import com.example.simplebooster.data.FeedMessage
import retrofit2.Call
import retrofit2.http.GET

interface MessageService {
    @GET("weibo")
    fun getFeedMessage(): Call<List<FeedMessage>>

    @GET("refresh")
    fun getRefreshMessage(): Call<List<FeedMessage>>
}