package com.example.pdmdiogo.News.data.repository

import com.example.pdmdiogo.News.data.remote.api.CoinPaprikaApi
import com.example.pdmdiogo.News.domain.model.Coin
import com.example.pdmdiogo.News.domain.model.CoinDetail
import com.example.pdmdiogo.News.domain.repository.CoinRepository

class CoinRepositoryImpl(private val api: CoinPaprikaApi): CoinRepository {
    override suspend fun getCoins(): List<Coin> {
        return api.getCoins().map {it.toCoin() }
    }
    override suspend fun getCoinDetail(coinId: String): CoinDetail {
        return api.getCoinDetail(coinId).toCoinDetail()
    }
}