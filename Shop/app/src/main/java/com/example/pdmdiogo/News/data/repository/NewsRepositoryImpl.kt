package com.example.pdmdiogo.News.data.repository

import android.util.Log
import com.example.pdmdiogo.News.data.remote.api.NewsApi
import com.example.pdmdiogo.News.domain.model.News
import com.example.pdmdiogo.News.domain.repository.NewsRepository

class NewsRepositoryImpl(private val api: NewsApi) : NewsRepository {
    override suspend fun getTopHeadlines(apiKey: String): List<News> {
        return try {
            val response = api.getTopHeadlines(apiKey = apiKey)
            Log.d("GetTest0", response.results.size.toString())
            response.results.map { it.toNews() }
        } catch (e: Exception) {
            Log.e("GetTest1", e.toString())
            emptyList()
        }
    }
}