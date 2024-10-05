package com.tunafysh.androra1n

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunafysh.androra1n.ui.theme.Androra1nTheme
import kotlinx.coroutines.flow.MutableStateFlow

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
                    LogBox()
                }
            }
        }
    }
}

//TODO: Create an Icon for this app and add the Consolas font for the logs.
@Composable
fun LogBox() {
    var isVisible by remember { mutableStateOf(false)}
    val annotatedText = parseAnsiToAnnotatedString("\u001B[36m[INFO] This is a test.\n\u001B[33m[WARN] This is a warning.\n\u001B[31m[ERROR] Something went wrong ig...")
     Box(
         modifier = Modifier
             .fillMaxSize(),
         contentAlignment = Alignment.Center
     ) {
         if(!isVisible){
             Button(onClick = { isVisible = !isVisible }) { Text("Show logs")}
         }
         AnimatedVisibility(visible = isVisible, modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))) {
              Box(
                  modifier = Modifier
                        .size(450.dp, 225.dp)
//                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(10.dp, 5.dp)
                        .clickable { isVisible = !isVisible }
                )
                {
                    Text(text = annotatedText, color = Color.White, fontFamily = FontFamily(Font(R.font.consolas)), style = TextStyle(letterSpacing = 0.5.sp))
                }
            }
        }
    }

fun parseAnsiToAnnotatedString(input: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var currentIndex = 0
    val ansiRegex = Regex("""\u001b\[(\d+)(;\d+)*m""")

    while (currentIndex < input.length) {
        if (input[currentIndex] == '\u001B') {
            val endIndex = input.indexOf('m', currentIndex)
            if (endIndex != -1) {
                val matchResult = ansiRegex.find(input, currentIndex)
                if (matchResult != null) {
                    val (firstCode, otherCodes) = matchResult.destructured
                    val codes = listOf(firstCode) + otherCodes.split(";").filter { it.isNotEmpty() }
                        val styles = codes.map { code ->
                            when (code.toIntOrNull()) {
                                30, 90 -> SpanStyle(color = Color.Black)
                                31, 91 -> SpanStyle(color = Color.Red)
                                32, 92 -> SpanStyle(color = Color.Green)
                                33, 93 -> SpanStyle(color = Color.Yellow)
                                34, 94 -> SpanStyle(color = Color.Blue)
                                35, 95 -> SpanStyle(color = Color.Magenta)
                                36, 96 -> SpanStyle(color = Color.Cyan)
                                37, 97 -> SpanStyle(color = Color.White)
                                1 -> SpanStyle(fontWeight = FontWeight.ExtraBold)
                                0 -> SpanStyle(color = Color.White, fontWeight = FontWeight.Normal)
                                // Add more ANSI codes as needed
                                else -> SpanStyle()
                            }
                        }

                        styles.forEach { builder.pushStyle(it) }
                        currentIndex = matchResult.range.last + 1
                } else {
                    // Handle non-ANSI text
                    currentIndex++
                }
            }
        } else {
            builder.append(input[currentIndex])
            currentIndex++
        }
    }
    return builder.toAnnotatedString()
}
@Composable
fun ExecuteCommandAndDisplayOutput() {
    val outputFlow = MutableStateFlow("")
    val outputText = outputFlow.collectAsState().value
    // Execute the command in a background thread
    Thread {
        try {
            val processBuilder = ProcessBuilder("ls")
            val process = processBuilder.start()
            val reader = process.inputStream.bufferedReader()
            while (true) {
                val line = reader.readLine() ?: break
                outputFlow.value += line + "\n"
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }.start()
    Text(text= outputText, color = Color.White)
}