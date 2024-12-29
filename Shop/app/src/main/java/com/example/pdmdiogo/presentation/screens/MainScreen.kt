package com.example.pdmdiogo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pdmdiogo.presentation.viewmodels.AuthViewModel
import com.example.pdmdiogo.presentation.viewmodels.ShoppingListViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun MainScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val user = Firebase.auth.currentUser
    val activeShoppingList = shoppingListViewModel.activeShoppingList
    val hasActiveList = shoppingListViewModel.hasActiveList

    var newListName by remember { mutableStateOf("") }

    user?.let {
        LaunchedEffect(Unit) {
            shoppingListViewModel.getActiveShoppingList(user.uid)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasActiveList) {
                Button(onClick = { navController.navigate("shopping_list/${activeShoppingList!!.id}/true") }) {
                    Text("Open List")
                }
            } else {
                TextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("List Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    shoppingListViewModel.createShoppingList(newListName, user.uid)
                    newListName = ""
                }) {
                    Text("Create New List")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("shared_lists") }) {
                Text("View Shared Lists")
            }

            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = {
                authViewModel.signOut()
                navController.navigate("login") { popUpTo("main") { inclusive = true } }
            }) {
                Text("Logout")
            }
        }
    }
}
