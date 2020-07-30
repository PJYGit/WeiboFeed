package com.example.simplebooster.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://www.fastmock.site/mock/c48033378af9b17b4e1c13eda02f52cb/test/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // inline fun <reified T> create(): T = create(T::class.java)
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}