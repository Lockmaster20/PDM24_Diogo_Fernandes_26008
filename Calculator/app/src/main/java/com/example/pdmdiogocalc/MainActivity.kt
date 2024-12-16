package com.example.pdmdiogocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pdmdiogocalc.Calculator.First

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            First()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    First()
}