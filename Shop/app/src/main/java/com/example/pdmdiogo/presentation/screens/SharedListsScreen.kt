package com.example.pdmdiogo.presentation.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pdmdiogo.MainActivity
import com.example.pdmdiogo.presentation.viewmodels.ShoppingListViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SharedListsScreen(navController: NavController) {
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val user = Firebase.auth.currentUser
    val sharedLists = shoppingListViewModel.sharedLists

    user?.let {
        LaunchedEffect(Unit) {
            shoppingListViewModel.getSharedShoppingLists(user.uid)
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Shared Lists",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
            Row {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(sharedLists) { list ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${list.name}", fontSize = 24.sp)
                            Text("Owner Id: ${list.ownerId}")
                            Button(onClick = { navController.navigate("shopping_list/${list.id}/false") }) {
                                Text("Open List")
                            }
                        }
                    }
                }
            }
        }
    }
}