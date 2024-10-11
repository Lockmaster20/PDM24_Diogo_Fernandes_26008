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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.exmaquina.ui.theme.ExMaquinaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Keyboard()
        }
    }
}

@Composable
fun Keyboard() {

    var tst by remember { mutableStateOf(0) }

    Column {
        Row {
            Text(text = tst.toString())
        }
        //BtnRow(listOf(("7", ::Number(7)); ("7", ::Number(7)), ("7", ::Number(7)), ("7", ::Number(7))))
        //BtnRow("7", ::Number(7))
        /*
        Row {
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
        */
    }
}

fun Number(num: Int){

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Keyboard()
}