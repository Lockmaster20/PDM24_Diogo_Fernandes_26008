package com.example.pdmdiogo.News.data.remote.model

import com.example.pdmdiogo.News.domain.model.News

data class NewsDto(
    val title: String,
    val description: String?,
    val content: String?
) {
    fun toNews(): News {
        return News(
            title = title,
            description = description ?: "No description." ,
            content = content ?: "No content.")
    }
}