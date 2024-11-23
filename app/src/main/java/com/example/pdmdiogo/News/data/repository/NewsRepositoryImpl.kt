package com.example.pdmdiogo.News.data.repository

import com.example.pdmdiogo.News.data.remote.api.NewsApi
import com.example.pdmdiogo.News.domain.model.News
import com.example.pdmdiogo.News.domain.repository.NewsRepository

class NewsRepositoryImpl(private val api: NewsApi) : NewsRepository {
    override suspend fun getTopHeadlines(apiKey: String): List<News> {
        return api.getTopHeadlines(apiKey = apiKey).articles.map {it.toNews() }
    }
}