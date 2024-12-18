package com.example.pdmdiogonews

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pdmdiogonews.News.domain.model.News
import com.example.pdmdiogonews.News.presentation.news_list.NewsListViewModel
import com.google.gson.Gson

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val newsListViewModel: NewsListViewModel = viewModel()

    //newsListViewModel.fetchNews(apiKey = "66af62a8906f4bbbbc4c2baeda1799c4")
    newsListViewModel.fetchNews(apiKey = "W33amOD3AMI2gnXGdx5eGzg8B9XVQG6n")

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
    Log.d("ScreenTest", newsList.toString())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Top Stories",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 30.sp)
        Divider(
            color = Color.Gray,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(newsList.value) { article ->
                Card(colors = CardDefaults.cardColors(
                    contentColor = Color.Black
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            val articleJson = Uri.encode(Gson().toJson(article))
                            navController.navigate("detail/$articleJson")
                        },
                    border = BorderStroke(2.dp, Color.Gray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(article.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp)
                        Text(article.description)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsDetailScreen(article: News) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = article.title,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp)
        Divider(
            color = Color.Gray,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        //Text(article.content,
        Text(article.description,
            color = Color.White)
    }
}