package com.example.pdmdiogonews.News.domain.repository

import com.example.pdmdiogonews.News.domain.model.News

interface NewsRepository {
    suspend fun getTopHeadlines(apiKey: String): List<News>
}