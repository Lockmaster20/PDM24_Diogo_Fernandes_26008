package com.example.pdmdiogo.News.data.remote.model

import com.example.pdmdiogo.News.domain.model.News

data class NewsDto(
    val title: String,
    val abstract: String?,
    val url: String?
) {
    fun toNews(): News {
        return News(
            title = title,
            description = abstract ?: "No description." ,
            content = url ?: "No content.")
    }
}

data class NewsApiResponse(
    val results: List<NewsDto>
)