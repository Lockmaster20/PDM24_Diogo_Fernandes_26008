package com.example.pdmdiogo.News.data.remote.api

import com.example.pdmdiogo.News.data.remote.model.NewsApiResponse
import com.example.pdmdiogo.News.data.remote.model.NewsDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitInstance {
    val api: NewsApi by lazy {
        Retrofit.Builder()
            //.baseUrl("https://newsapi.org/")
            .baseUrl("https://api.nytimes.com/svc/topstories/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}
interface NewsApi {
    @GET("home.json")
    suspend fun getTopHeadlines(
        @Query("api-key") apiKey: String
    ): NewsApiResponse
}
/*
interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String
    ): NewsApiResponse
}*/