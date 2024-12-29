package com.example.pdmdiogo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pdmdiogo.presentation.screens.LoginScreen
import com.example.pdmdiogo.presentation.screens.MainScreen
import com.example.pdmdiogo.presentation.screens.RegisterScreen
import com.example.pdmdiogo.presentation.screens.SharedListsScreen
import com.example.pdmdiogo.presentation.screens.ShoppingListScreen

@Composable
fun Shop() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("shared_lists") { SharedListsScreen(navController) }
        composable("shopping_list/{listId}/{isOwner}", arguments = listOf(
            navArgument("listId") { type = NavType.StringType },
            navArgument("isOwner") { type = NavType.BoolType }
        )) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: ""
            val isOwner = backStackEntry.arguments?.getBoolean("isOwner") ?: false
            ShoppingListScreen(navController, listId, isOwner)
        }
    }
}