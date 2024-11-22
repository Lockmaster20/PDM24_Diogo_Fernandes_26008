package com.example.pdmdiogo.News.domain.repository

import com.example.pdmdiogo.News.domain.model.News

interface NewsRepository {
    suspend fun getTopHeadlines(apiKey: String): List<News>
}