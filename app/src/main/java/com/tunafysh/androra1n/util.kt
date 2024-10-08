package com.tunafysh.androra1n

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

class Parameters {
    var rootful =false
    var create_fakefs =false
    var create_bindfs =false
    var clean_fakefs =false
    var verbose =true
    var safe_mode =false
    var revert_install =false
    var serial =false
    var debug =false
}

fun parse(params: Parameters): String {
    var argsarray = arrayOf("").toMutableList()

    if (params.rootful) argsarray.add("-f")
    if (params.create_fakefs && params.rootful) argsarray.add("-c")
    if (params.create_bindfs && params.rootful) argsarray.add("-B")
    if (params.clean_fakefs && params.rootful) argsarray.add("-C")
    if (params.verbose) argsarray.add("-V")
    if (params.safe_mode) argsarray.add("-s")
    if (params.revert_install) argsarray.add("--force-revert")
    if (params.serial) argsarray.add("serial=3")
    if (params.debug) argsarray.add("-v")

    var args: String = argsarray.joinToString(" ")
    return args
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

//fun bootstrap(configlocation: String) {
//
//}