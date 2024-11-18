package com.example.pdmdiogo.News.data.remote.model

import com.example.pdmdiogo.News.domain.model.CoinDetail

data class CoinDetailDto(
    val id: String,
    val name: String,
    val description: String
) {
    fun toCoinDetail(): CoinDetail {
        return CoinDetail(id = id, name = name, description = description)
    }
}