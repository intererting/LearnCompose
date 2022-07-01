package com.yly.learncompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yly.learncompose.ui.theme.LearnComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                ) {
//                    HelloScreen()
//                    OutterComposable.HelloScreen()
                    PasswordTextField()
                }
            }
        }
    }
}

@Composable
fun PasswordTextField() {
    var password by rememberSaveable { mutableStateOf("") }

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Enter password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun AnnotatedClickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click ")

        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://developer.android.com"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("here")
        }

        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            // We check if there is an *URL* annotation attached to the text
            // at the clicked position
            annotatedText.getStringAnnotations(
                tag = "URL", start = offset,
                end = offset
            )
                .firstOrNull()?.let { annotation ->
                    // If yes, we log its value
                    Log.d("Clicked URL", annotation.item)
                }
        }
    )
}

@Composable
fun SimpleClickableText() {
    ClickableText(
        text = AnnotatedString("Click Me"),
        onClick = { offset ->
            Log.d("ClickableText", "$offset -th character is clicked.")
        }
    )
}

@Composable
@Stable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LearnComposeTheme {
//        Greeting("compose")
    }
}

val DataSaver = run {
    val nameKey = "ID"
    mapSaver(
        save = { mapOf(nameKey to it.id) },
        restore = { SaveData(it[nameKey] as String) }
    )
}


@Composable
fun HelloScreen(viewModels: HelloViewModel = viewModel()) {

    //rememberSaveable可以保存configuration的状态
    var saveable by rememberSaveable(inputs = emptyArray()) {
        //支持的只能是Parcelize序列化后的对象
        mutableStateOf(SaveData("default"))
    }

    //只能保存普通的状态
    var rememberName by remember {
        mutableStateOf("default")
    }

//    //将状态保存到ViewModel中
    val viewModelState by viewModels.liveData.observeAsState("default")

//    var mapSaver = rememberSaveable(stateSaver = DataSaver) {
//        mutableStateOf(SaveData(1001))
//    }

    HelloContent(
        name = rememberName,
        saveData = saveable,
        viewModelState = viewModelState,
        onValueChange = {
            viewModels.changeText(it)
            saveable = SaveData(it)
            rememberName = it
        })

}

@Composable
fun HelloContent(
    name: String,
    saveData: SaveData,
    viewModelState: String,
    onValueChange: (String) -> Unit
) {
    println("composing")
    Column(Modifier.padding(8.dp)) {
        Text(text = name, modifier = Modifier.padding(6.dp))
        Text(text = saveData.id)
        Text(text = viewModelState)
        OutlinedTextField(value = name, onValueChange = {
            onValueChange(it)
        })
        Divider(modifier = Modifier.padding(top = 5.dp), thickness = 5.dp, color = Color.Red)
        UsingKey(items = listOf("item-1", "item-2", "item-3"))
    }
}

@Composable
fun UsingKey(items: List<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (item in items) {
            //key可以复用
            key(item) {
                Text(text = item)
            }
        }
    }
}






















