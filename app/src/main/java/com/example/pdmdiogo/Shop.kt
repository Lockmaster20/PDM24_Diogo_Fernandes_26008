package com.example.pdmdiogo

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("register") { RegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("main") { MainScreen(navController) }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    val mainActivity = LocalContext.current as MainActivity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text("Register")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {mainActivity.registerUserWithFirebase(email, password)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("login") { }
            }
        }}) {
            Text("Register")
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val mainActivity = LocalContext.current as MainActivity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Text("Login")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {mainActivity.signInUserWithFirebase(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }}) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
                    navController.navigate("register") {}
                }) {
            Text("Register")
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val user = Firebase.auth.currentUser
    Column {
        Text("Teste, ${user?.email}")
        Button(onClick = {
            Firebase.auth.signOut()
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}