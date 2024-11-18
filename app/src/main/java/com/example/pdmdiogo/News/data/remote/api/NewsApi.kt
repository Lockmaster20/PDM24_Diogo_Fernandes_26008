package com.example.pdmdiogo.News.data.remote.api

import com.example.pdmdiogo.News.data.remote.model.CoinDetailDto
import com.example.pdmdiogo.News.data.remote.model.CoinDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitInstance {
    val api: CoinPaprikaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coinpaprika.com/")
            .addConverterFactory (GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaApi::class.java)
    }
}

interface CoinPaprikaApi {
    @GET("v1/coins")
    suspend fun getCoins(): List<CoinDto>
    @GET("v1/coins/{id}")
    suspend fun getCoinDetail(@Path("id") coinId: String): CoinDetailDto
}