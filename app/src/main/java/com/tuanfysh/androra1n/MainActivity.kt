package com.tuanfysh.androra1n

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tuanfysh.androra1n.ui.theme.Androra1nTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Androra1nTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Greeting(
                        name = "world",
                        modifier = Modifier
                    )
                    ShowHideBoxExample()
                }
            }
        }
    }
}

@Composable
fun ShowHideBoxExample() {
    var isVisible by remember { mutableStateOf(false)}

    Scaffold(
        floatingActionButton = {
            FloatingActionButton (onClick = { isVisible = !isVisible }) {
                Icon(Icons.Default.Add, contentDescription = "Toggle Box")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (isVisible) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp,MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Hello, world!", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface
    )
}

fun idk(content: String): String {
    return "Hello $content!"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Androra1nTheme {
        FloatingActionButton(onClick = {}) { }
        Greeting("Test")
    }
}