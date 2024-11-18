package com.example.pdmdiogo.News.domain.repository

import com.example.pdmdiogo.News.domain.model.Coin
import com.example.pdmdiogo.News.domain.model.CoinDetail

interface CoinRepository {
    suspend fun getCoins(): List<Coin>
    suspend fun getCoinDetail (coinId: String): CoinDetail
}