package com.example.pdmdiogo.News.data.remote.model

import com.example.pdmdiogo.News.domain.model.Coin

data class CoinDto(
    val id: String,
    val name: String,
    val symbol: String
) {
    fun toCoin(): Coin {
        return Coin(id = id, name = name, symbol = symbol)
    }
}