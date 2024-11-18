package com.example.pdmdiogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pdmdiogo.News.presentation.news_list.CoinListViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pdmdiogo.News.domain.model.Coin
import androidx.compose.runtime.LaunchedEffect


@Composable
fun MainScreen() {
    var selectedCoinId by remember { mutableStateOf<String?>(null) }

    val coinListViewModel: CoinListViewModel = viewModel()
    CoinListScreen(coinListViewModel) { coinId ->
        selectedCoinId = coinId
    }
}


@Composable
fun CoinListScreen(
    viewModel: CoinListViewModel, // ViewModel instance
    onCoinSelected: (String) -> Unit // Callback when a coin is selected
) {
    // Collect the state of the coins list
    val coins by viewModel.coins.collectAsState()

    // Fetch the coins when the screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchCoins()
    }

    // Display the list
    LazyColumn {
        items(coins) { coin ->
            // Define how each coin item looks
            CoinItem(
                coin = coin,
                onClick = { onCoinSelected(coin.id) }
            )
        }
    }
}

@Composable
fun CoinItem(
    coin: Coin, // The coin item to display
    onClick: () -> Unit // Callback when the item is clicked
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = coin.name, // Assuming the Coin object has a 'name' property
            modifier = Modifier.weight(1f)
        )
    }
}
