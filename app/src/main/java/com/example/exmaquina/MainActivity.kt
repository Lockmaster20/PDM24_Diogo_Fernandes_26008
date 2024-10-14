package com.example.exmaquina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.sp
import com.example.exmaquina.ui.theme.ExMaquinaTheme
import kotlin.math.sqrt

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

    var memory by remember { mutableStateOf("0") }
    var display by remember { mutableStateOf("0") }
    var operation by remember { mutableStateOf("") }

    fun Click(value: String){
        //memory += value.toInt()
        //display = memory
        when (value){
            "÷", "×", "-", "+" -> {
                memory = Calculate(memory.toDouble(), display.toDouble(), operation).toString()
                display = "0"
                operation = value
            }
            "=" -> {
                display = Calculate(memory.toDouble(), display.toDouble(), operation).toString()
                memory = "0"
            }
            "CE" -> {
                display = "0"
            }
            "C" -> {
                display = "0"
                memory = "0"
                operation = ""
            }
            "." -> {
                if(!display.contains(".")){
                    val join = "$display."
                    display = join
                }
            }
            "√" -> {
                memory = "0"
                display = Calculate(0.0, display.toDouble(), value).toString()
            }
            else -> {
                var join = "0"
                if(display == "0"){
                    join = "$value"
                } else {
                    join = "$display$value"
                }
                display = join
            }
        }
    }

    Column{
        Row (modifier = Modifier.padding(24.dp)){
            Text(text = "$display", fontSize = 48.sp)
        }
        SetButtons(listOf("MRC", "M-", "M+", "C", "√", "%", "±", "CE", "7", "8", "9", "÷", "4", "5", "6", "×", "1", "2", "3", "-", "0", ".", "=", "+"), ::Click)
    }
}

fun Calculate(memory: Double, display: Double, operation: String): Double{
    var result : Double = 0.0
    when (operation){
        "÷" -> {
            result = (memory/display).toDouble()
        }
        "×" -> {
            result = (memory*display).toDouble()
        }
        "-" -> {
            result = (memory-display).toDouble()
        }
        "+" -> {
            result = (memory+display).toDouble()
        }
        "√" -> {
            result = sqrt(display).toDouble()
        }
        else -> {
            result = display
        }
    }
    return result
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