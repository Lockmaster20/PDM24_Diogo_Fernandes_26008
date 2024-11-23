package com.example.pdmdiogo

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.pdmdiogo.News.presentation.news_list.CoinListViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import com.example.pdmdiogo.News.domain.model.Coin
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pdmdiogo.News.domain.model.News
import com.example.pdmdiogo.News.presentation.news_list.NewsListViewModel
import com.google.gson.Gson

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val newsListViewModel: NewsListViewModel = viewModel()

    newsListViewModel.fetchNews(apiKey = "66af62a8906f4bbbbc4c2baeda1799c4")

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            NewsListScreen(navController, newsListViewModel)
        }
        composable("detail/{article}"){ backStackEntry ->
            val articleJson = backStackEntry.arguments?.getString("article")
            if (articleJson != null) {
                val article = Gson().fromJson(articleJson, News::class.java)
                NewsDetailScreen(article)
            }
        }
    }
}

@Composable
fun NewsListScreen(navController: NavController,  viewModel: NewsListViewModel){
    val newsList = viewModel.news.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Top News")
        Spacer(modifier = Modifier.height(16.dp))

        Text("a")
        newsList.value.forEach { article ->
            Text("b")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        val articleJson = Uri.encode(Gson().toJson(article))
                        navController.navigate("detail/$articleJson")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(article.title)
                    Text(article.description)
                }
            }
        }
        Text("c")
    }
}

@Composable
fun NewsDetailScreen(article: News) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Details")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Title: ${article.title}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Description: ${article.description}")
        Spacer(modifier = Modifier.height(16.dp))
    }
}