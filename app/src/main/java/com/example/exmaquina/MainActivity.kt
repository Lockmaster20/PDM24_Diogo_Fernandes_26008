package com.example.exmaquina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.exmaquina.ui.theme.ExMaquinaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        }
    }
}

@Composable
fun Keyboard() {
    Column {
        Row {
            Text("1")
        }
        Row {
            // onClick = { test("Hello") }
            Button(onClick = {}) {
                Text("7")
            }
            Button(onClick = {}) {
                Text("8")
            }
            Button(onClick = {}) {
                Text("9")
            }
            Button(onClick = {}) {
                Text("÷")
            }
        }
        Row {
            // onClick = { test("Hello") }
            Button(onClick = {}) {
                Text("4")
            }
            Button(onClick = {}) {
                Text("5")
            }
            Button(onClick = {}) {
                Text("6")
            }
            Button(onClick = {}) {
                Text("×")
            }
        }
        Row {
            Button(onClick = {}) {
                Text("1")
            }
            Button(onClick = {}) {
                Text("2")
            }
            Button(onClick = {}) {
                Text("3")
            }
            Button(onClick = {}) {
                Text("-")
            }
        }
        Row {
            Button(onClick = {}) {
                Text("0")
            }
            Button(onClick = {}) {
                Text(".")
            }
            Button(onClick = {}) {
                Text("=")
            }
            Button(onClick = {}) {
                Text("+")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Keyboard()
}