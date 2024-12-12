package com.example.pdmdiogo.News.domain.use_case

import com.example.pdmdiogo.News.domain.model.News
import com.example.pdmdiogo.News.domain.repository.NewsRepository

class GetNewsUseCase (private val repository: NewsRepository) {
    suspend operator fun invoke(apiKey: String): List<News> {
        return repository.getTopHeadlines(apiKey)
    }
}