package com.example.pdmdiogo.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pdmdiogo.domain.model.Product
import com.example.pdmdiogo.domain.model.User
import com.example.pdmdiogo.presentation.viewmodels.ProductViewModel
import com.example.pdmdiogo.presentation.viewmodels.ShoppingListViewModel
import com.example.pdmdiogo.presentation.viewmodels.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ShoppingListScreen(navController: NavController, listId: String, isOwner: Boolean ) {
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val context = LocalContext.current
    val user = Firebase.auth.currentUser

    val productsList = productViewModel.productsList
    val usersList = userViewModel.usersList
    val listItems = shoppingListViewModel.listItems
    val shoppingList = shoppingListViewModel.shoppingList

    val itemDialog = remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var textFieldValueProduct by remember { mutableStateOf("") }

    val usersDialog = remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var textFieldValueUser by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    val payDialog = remember { mutableStateOf(false) }

    user?.let {
        LaunchedEffect(Unit) {
            productViewModel.loadProducts()
            userViewModel.loadUsers()
            shoppingListViewModel.getShoppingListById(listId)
            shoppingListViewModel.getShoppingListItems(listId)
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = "List: ${shoppingList?.name}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
                Row {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { itemDialog.value = true }) {
                        Text("Add Item")
                    }
                    if (isOwner) {
                        Button(onClick = { usersDialog.value = true }) {
                            Text("Share List")
                        }
                        Button(onClick = { payDialog.value = true }) {
                            Text("Finish List")
                        }
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(listItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.name, fontSize = 24.sp)
                                Text(item.description)
                                Text("Price: ${item.price}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Button(onClick = {
                                    shoppingListViewModel.removeItemFromList(listId, item.id)
                                }) {
                                    Text("Remove")
                                }
                            }
                        }
                    }
                }

            if (itemDialog.value) {
                AlertDialog(
                    onDismissRequest = { itemDialog.value = false },
                    title = {
                        Text(
                            text = "Add Item",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column {
                            Text(text = "Select an item to add to the list.")
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                    Text(
                                        text = "Product: ${selectedProduct?.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expanded = true }
                                            .padding(8.dp)
                                            .background(
                                                Color.LightGray,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        productsList.forEach { product ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedProduct = product
                                                    textFieldValueProduct =
                                                        product.name
                                                    expanded = false
                                                },
                                                text = { Text(product.name) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (selectedProduct != null) {
                                shoppingListViewModel.addProductToList(listId, selectedProduct!!.id) { success ->
                                    if (success) {
                                        shoppingListViewModel.getShoppingListItems(listId)
                                        itemDialog.value = false
                                        Toast.makeText(
                                            context,
                                            "Product added successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error adding product",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }) {
                            Text(text = "Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { itemDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }

            if (usersDialog.value) {
                AlertDialog(
                    onDismissRequest = { usersDialog.value = false },
                    title = {
                        Text(
                            text = "Share List",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column {
                            Text(text = "Select a user to share the list with.")
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                    Text(
                                        text = "User: ${selectedUser?.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expanded = true }
                                            .padding(8.dp)
                                            .background(
                                                Color.LightGray,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        usersList.forEach { user ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedUser = user
                                                    textFieldValueUser =
                                                        user.name
                                                    expanded = false
                                                },
                                                text = { Text(user.name) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (selectedUser != null) {
                                if (selectedUser!!.id != user.uid){
                                    shoppingListViewModel.shareList(listId, selectedUser!!.id) { success ->
                                        if (success) {
                                            usersDialog.value = false
                                            Toast.makeText(
                                                context,
                                                "List shared successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error sharing the list",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }) {
                            Text(text = "Share")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { usersDialog.value = false }) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }

            if (payDialog.value) {
                AlertDialog(
                    onDismissRequest = { payDialog.value = false },
                    title = {
                        Text(
                            text = "Payment",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Select payment method to finish."
                        )
                    },
                    confirmButton = {
                        Row (
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { payDialog.value = false
                                shoppingListViewModel.finishShoppingList(listId) { navController.popBackStack() }}) {
                                Text(text = "Pay with A")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = { payDialog.value = false
                                shoppingListViewModel.finishShoppingList(listId) { navController.popBackStack() }}) {
                                Text(text = "Pay with B")
                            }
                        }
                    },
                    dismissButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = { payDialog.value = false }) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                )
            }
        }

    }
}
