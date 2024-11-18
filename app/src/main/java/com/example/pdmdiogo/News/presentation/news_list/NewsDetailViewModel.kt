package com.example.pdmdiogo.News.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.News.data.remote.api.RetrofitInstance
import com.example.pdmdiogo.News.data.repository.CoinRepositoryImpl
import com.example.pdmdiogo.News.domain.model.CoinDetail
import com.example.pdmdiogo.News.domain.use_case.GetCoinsDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CoinDetailViewModel: ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = CoinRepositoryImpl(api)
    private val getCoinDetailUseCase = GetCoinsDetailUseCase(repository)

    val coinDetail = MutableStateFlow<CoinDetail?>(null)

    fun fetchCoinDetail(coinId: String) {
        viewModelScope.launch {
            try {
                coinDetail.value = getCoinDetailUseCase(coinId)
            } catch (e: Exception) {
                coinDetail.value = null
            }
        }
    }
}