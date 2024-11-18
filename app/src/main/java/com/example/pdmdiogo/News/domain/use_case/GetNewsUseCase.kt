package com.example.pdmdiogo.News.domain.use_case

import com.example.pdmdiogo.News.domain.model.Coin
import com.example.pdmdiogo.News.domain.model.CoinDetail
import com.example.pdmdiogo.News.domain.repository.CoinRepository

class GetCoinsUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(): List<Coin> {
        return repository.getCoins()
    }
}

class GetCoinsDetailUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(coinId: String): CoinDetail {
        return repository.getCoinDetail(coinId)
    }
}