package com.example.pdmdiogonews.News.domain.use_case

import com.example.pdmdiogonews.News.domain.model.News
import com.example.pdmdiogonews.News.domain.repository.NewsRepository

class GetNewsUseCase (private val repository: NewsRepository) {
    suspend operator fun invoke(apiKey: String): List<News> {
        return repository.getTopHeadlines(apiKey)
    }
}