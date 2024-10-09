package com.tunafysh.androra1n

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunafysh.androra1n.ui.theme.Androra1nTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Androra1nTheme {
                MainMenu()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MainMenu() {
    var rootful by remember { mutableStateOf(false) }
    var createFakefs by remember { mutableStateOf(false) }
    var createBindfs by remember { mutableStateOf(false) }
    var cleanFakefs by remember { mutableStateOf(false) }
    var verbose by remember { mutableStateOf(true) }
    var safeMode by remember { mutableStateOf(false) }
    var revertInstall by remember { mutableStateOf(false) }
    var serial by remember { mutableStateOf(false) }
    var debug by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var scrollState = rememberScrollState()
    Scaffold (
        modifier = Modifier,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(0.dp, 30.dp,0.dp,0.dp), contentAlignment = Alignment.CenterStart){
                var params = Parameters()
                params.rootful = rootful
                params.serial = serial
                params.verbose = verbose
                params.debug = debug
                params.safe_mode = safeMode
                params.revert_install = revertInstall
                params.create_bindfs = createBindfs
                params.create_fakefs = createFakefs
                params.clean_fakefs = cleanFakefs
                var parsedParams = parse(params)
                Text(text = "Arguments: "+parsedParams, modifier = Modifier.horizontalScroll(rememberScrollState()).width(300.dp) )
            }
            Box( modifier = Modifier.fillMaxWidth().padding(0.dp,30.dp,0.dp,0.dp), contentAlignment = Alignment.CenterEnd) {

                LogBox()
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            Button(onClick = {showBottomSheet = true} ) {
                    Text("Arguments")
            }
        }
        if(showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {showBottomSheet = false}, modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(7.dp)
                )
                {
                    rootful = jailbreakTypeSelector(rootful)
                    AnimatedVisibility(visible = rootful) {
                        Column {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Create FakeFS")
                            Switch(checked = createFakefs, onCheckedChange = {createFakefs = it})
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Create BindFS")
                            Switch(checked = createBindfs, onCheckedChange = {createBindfs = it})
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Clean FakeFS")
                            Switch(checked = cleanFakefs, onCheckedChange = {cleanFakefs = it})
                        }
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Verbose")
                        Switch(checked = verbose, onCheckedChange = {verbose = it})
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Safe mode")
                        Switch(checked = safeMode, onCheckedChange = {safeMode = it})
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Revert install")
                        Switch(checked = revertInstall, onCheckedChange = {revertInstall = it})
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Serial")
                        Switch(checked = serial, onCheckedChange = {serial = it})
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Debug")
                        Switch(checked = debug, onCheckedChange = {debug = it})
                    }
                }
            }
        }
    }
}

@Composable
fun jailbreakTypeSelector( root: Boolean): Boolean {
    var rootful by remember { mutableStateOf(root) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("Jailbreak type:")
        Column {

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
            RadioButton(
                selected = ( rootful == false ),
                onClick = { rootful = false }
            )
            Text("Rootless")
        }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
            RadioButton(
                selected = (rootful == true),
                onClick = { rootful = true }
            )
            Text("Rootful")
        }
        }
    }
    return rootful
}


@Composable
fun LogBox() {
    var isVisible by remember { mutableStateOf(false)}
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var annotatedText = parseAnsiToAnnotatedString("\u001B[1;36mTest")
         if(!isVisible){
             Button(onClick = { isVisible = !isVisible }) { Text("Show logs")}
         }
         if(isVisible){
              Box(
                  modifier = Modifier
                      .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt())}
                      .pointerInput(Unit) {
                          detectDragGestures { change, dragAmount ->
                              change.consume()
                              offsetX += dragAmount.x
                              offsetY += dragAmount.y
                          }
                      }
                      .clickable { isVisible = !isVisible }
                      .size(450.dp, 225.dp)
                      .fillMaxSize()

                      .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .padding(10.dp, 5.dp)
              )
                {
                    Text(text = annotatedText, color = Color.White, fontFamily = FontFamily(Font(R.font.consolas)), style = TextStyle(letterSpacing = 0.5.sp))
                }
            }
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