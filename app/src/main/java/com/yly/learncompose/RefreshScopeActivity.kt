package com.yly.learncompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

/**
 * @author    yiliyang
 * @date      2022/8/29 下午4:07
 * @version   1.0
 * @since     1.0
 */
class RefreshScopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyContent()
        }
    }
}

@Composable
private fun MyContent() {
    val text = remember {
        mutableStateOf("default")
    }
    Column {
        MyContentInner(text = { text.value }) {
            text.value =
                "changed ${System.currentTimeMillis()} ${System.currentTimeMillis()} ${System.currentTimeMillis()} ${System.currentTimeMillis()}"
        }
        println("recompose 1")
        Text(text = "below")
    }
}

@Composable
private fun MyContentInner(text: () -> String, callback: () -> Unit) {
    Column {
        println("recompose 2")
        Text(text = text())
        Button(onClick = callback) {
            Text(text = "click")
        }
    }
}