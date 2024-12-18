package com.example.pdmdiogonews.News.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogonews.News.data.remote.api.RetrofitInstance
import com.example.pdmdiogonews.News.data.repository.NewsRepositoryImpl
import com.example.pdmdiogonews.News.domain.model.News
import com.example.pdmdiogonews.News.domain.use_case.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class NewsListViewModel: ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = NewsRepositoryImpl(api)
    private val getNewsUseCase = GetNewsUseCase(repository)

    val news = MutableStateFlow<List<News>>(emptyList())

    fun fetchNews (apiKey: String) {
        viewModelScope.launch{
            try {
                news.value = getNewsUseCase(apiKey)
            }
            catch (e: Exception) {
                news.value = emptyList()
            }
        }
    }
}