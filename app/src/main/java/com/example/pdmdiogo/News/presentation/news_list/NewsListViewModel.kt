package com.example.pdmdiogo.News.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdmdiogo.News.data.remote.api.RetrofitInstance
import com.example.pdmdiogo.News.data.repository.CoinRepositoryImpl
import com.example.pdmdiogo.News.domain.model.Coin
import com.example.pdmdiogo.News.domain.use_case.GetCoinsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CoinListViewModel: ViewModel() {
    private val api = RetrofitInstance.api
    private val repository = CoinRepositoryImpl(api)
    private val getCoinsUseCase = GetCoinsUseCase (repository)

    val coins = MutableStateFlow<List<Coin>> (emptyList())

    fun fetchCoins () {
        viewModelScope.launch{
            try {
                coins.value = getCoinsUseCase()
            }
            catch (e: Exception) {
                coins.value = emptyList()
            }
        }
    }
}