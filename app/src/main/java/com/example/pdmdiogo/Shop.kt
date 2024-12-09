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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/*
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "register") {
        composable("register") { RegisterScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("main") { MainScreen(navController) }
    }
}
*/

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
            Text("Login")
        }
    }
    Column {
        // On successful registration
        Button(onClick = { navController.navigate("login") }) {
            Text("Go to Login")
        }
    }
}

@Composable
fun Login() {
    val mainActivity = LocalContext.current as MainActivity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {

        Text("Welcome")
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
        //Button(onClick = {mainActivity.signInUserWithFirebase(email, password)}) {
        Button(onClick = {mainActivity.registerUserWithFirebase(email, password)}) {
            Text("Login")
        }
    }
}