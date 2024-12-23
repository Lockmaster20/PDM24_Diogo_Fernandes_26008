package com.example.pdmdiogo

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pdmdiogo.Shop.domain.model.Product
import com.example.pdmdiogo.Shop.domain.model.ShoppingList
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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

@Composable
fun RegisterScreen(navController: NavController) {
    val mainActivity = LocalContext.current as MainActivity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column {
        Text("Register")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text("Name") }
        )
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
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                mainActivity.registerUserWithFirebase(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = task.result?.user
                            user?.let {
                                mainActivity.saveUserData(user.uid, name, email)
                                    .addOnCompleteListener { task ->
                                        if (task.result == true) {
                                            Toast.makeText(mainActivity.baseContext, "Registration Data Success", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login") { }
                                        } else {
                                            Toast.makeText(mainActivity.baseContext, "Registration Data Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
                    }
            } else {
                Toast.makeText(mainActivity.baseContext, "Missing Fields", Toast.LENGTH_SHORT).show()
            }
        }) {
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
    val mainActivity = LocalContext.current as MainActivity
    val user = Firebase.auth.currentUser
    var activeShoppingList by remember { mutableStateOf<ShoppingList?>(null) }
    var hasActiveList by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    user?.let {
        LaunchedEffect(Unit) {
            mainActivity.getActiveShoppingList(user.uid) { list ->
                activeShoppingList = list
                hasActiveList = (list != null)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasActiveList) {
                Button(onClick = { navController.navigate("shopping_list/${activeShoppingList!!.id}/true") { } }) {
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
                    mainActivity.createShoppingList(user.uid, newListName) { success ->
                        if (success) {
                            // Atualiza variáveis da página para mostrar a lista
                            mainActivity.getActiveShoppingList(user.uid) { list ->
                                activeShoppingList = list
                                hasActiveList = (list != null)
                            }
                        }
                    }
                }) {
                    Text("Create New List")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("shared_lists") { } }) {
                Text("View Shared Lists")
            }

            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = {
                Firebase.auth.signOut()
                navController.navigate("login") { popUpTo("main") { inclusive = true } }
            }) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun SharedListsScreen(navController: NavController) {
    val mainActivity = LocalContext.current as MainActivity
    val user = Firebase.auth.currentUser
    var sharedLists by remember { mutableStateOf<List<ShoppingList>>(emptyList()) }

    user?.let {
        LaunchedEffect(Unit) {
            mainActivity.getSharedShoppingLists(user.uid) { lists ->
                sharedLists = lists
            }
        }

        Column {
            Text("Shared Lists")
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sharedLists) { list ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${list.name}", fontSize = 24.sp)
                        Text("Owner Id: ${list.ownerId}")
                        Button(onClick = { navController.navigate("shopping_list/${list.id}/false") { } }) {
                            Text("Open List")
                        }
                    }
                }
            }
        }
    }
}

// !!! Fazer o atualizar a lista depois de adicionar/remover produtos
@Composable
fun ShoppingListScreen(navController: NavController, listId: String, isOwner: Boolean) {
    val mainActivity = LocalContext.current as MainActivity
    val user = Firebase.auth.currentUser
    var productsList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var listItems by remember { mutableStateOf<List<Product>>(emptyList()) }
    var list by remember { mutableStateOf<ShoppingList?>(null) }

    user?.let {
        LaunchedEffect(Unit) {
            mainActivity.getProducts() { products ->
                productsList = products
            }
            mainActivity.getShoppingListById(listId) { result ->
                list = result
            }
            mainActivity.getShoppingListItems(listId) { items ->
                listItems = items
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "List: ${list?.name}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 5f,
                            offset = Offset(2f, 2f)
                        )
                    )
            )


            LazyColumn(modifier = Modifier.weight(1f)) {
                items(listItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.name, fontSize = 24.sp)
                            Text(item.description)
                            Text("Price: ${item.price}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Button(onClick = { mainActivity.removeItemFromList(listId, item.id) { success ->
                                if (success) {
                                    // !!! Atualiza lista
                                } else {
                                    // !!! Mensagem de erro
                                }
                            } }) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }
/*
        Column {
            Button(onClick = { }) { // !!! Fazer
                Text("Add Item")
            }
            Button(onClick = { }) { // !!! Fazer
                Text("Share List")
            }
            Button(onClick = {
                mainActivity.finishShoppingList(listId) { } // !!! Ao terminar lista redirecionar
            }) {
                Text("Finish List")
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
*/
    }
}