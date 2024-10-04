package com.tunafysh.androra1n

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunafysh.androra1n.ui.theme.Androra1nTheme

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
            var parsedString = parseAnsiToAnnotatedString("Hello, world!\n \u001B[36mtest")
            if (isVisible) {
                Box(
                    modifier = Modifier
                        .size(450.dp, 225.dp)
                        .border(1.dp,MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(10.dp, 5.dp)
                ) {
                    Text(text = parsedString , color = Color.White)
                }
            }
        }
    }
}

fun parseAnsiToAnnotatedString(input: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var currentIndex = 0

    while (currentIndex < input.length) {
        if (input[currentIndex] == '\u001B') {
            val endIndex = input.indexOf('m', currentIndex)
            if (endIndex != -1) {
                val code = input.substring(currentIndex + 2, endIndex).toIntOrNull()
                val style = when (code) {
                    30, 90 -> SpanStyle(color = Color.Black)
                    31, 91 -> SpanStyle(color = Color.Red)
                    32, 92 -> SpanStyle(color = Color.Green)
                    33, 93 -> SpanStyle(color = Color.Yellow)
                    34, 94 -> SpanStyle(color = Color.Blue)
                    35, 95 -> SpanStyle(color = Color.Magenta)
                    36, 96 -> SpanStyle(color = Color.Cyan)
                    37, 97 -> SpanStyle(color = Color.White)

                    // Add more ANSI codes as needed
                    else -> SpanStyle()
                }
                builder.pushStyle(style)
                currentIndex = endIndex + 1
            }
        } else {
            builder.append(input[currentIndex])
            currentIndex++
        }
    }
    return builder.toAnnotatedString()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Androra1nTheme {
        FloatingActionButton(onClick = {}) { }
        Greeting("Test")
    }
}