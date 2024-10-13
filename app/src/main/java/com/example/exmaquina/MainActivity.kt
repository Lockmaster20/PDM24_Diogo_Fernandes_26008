package com.example.exmaquina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.example.exmaquina.ui.theme.ExMaquinaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            First()
        }
    }
}

@Composable
fun First() {

    var memory by remember { mutableStateOf(0) }
    var display by remember { mutableStateOf(0) }

    fun Click(value: String){
        memory += value.toInt()
        display = memory
    }

    Column{
        Row {
            //Text("$display", Modifier.size(48.dp), fontSize = 48)
            Text("$display")
        }
        SetButtons(listOf("1", "2", "3", "4", "5", "6", "7", "8"), ::Click)
    }
}

@Composable
fun SetButtons(buttons: List<String>, onClick: (String) -> Unit){
    val rows = buttons.chunked(4)

    for(row in rows){
        Row {
            for(button in row) {
                Button(onClick = { onClick(button)}) {
                    Text(button)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    First()
}