package com.yly.learncompose

import android.os.Bundle
import android.widget.Space
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author    yiliyang
 * @date      2021/8/17 上午10:07
 * @version   1.0
 * @since     1.0
 */
class CompositionLocalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            var data by remember {
//                mutableStateOf(ParentData(1))
//            }
//            CompositionLocalProvider(LocalParentData provides data) {
//                Column {
//                    Button(onClick = {
//                        data = ParentData(100)
//                    }) {
//                        Text(text = "change")
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Child()
//                }
//            }

            CompositionLocalProvider(LocalParentData provides ParentData("default")) {
                Column {
                    Text(text = LocalParentData.current.version)
                    CompositionLocalProvider(LocalParentData provides ParentData("changed")) {
                        LocalParentData.current.name = "changed name"
                        Text(text = LocalParentData.current.version)
                        Text(text = LocalParentData.current.name)
                    }
                    Text(text = LocalParentData.current.name)
                }
            }
        }
    }
}

@Composable
fun Child() {
//    println("composing")
//    Text(text = "version ${LocalParentData.current.version}", color = MaterialTheme.colors.error)
//    LocalParentData.current.version = 1001
//    Spacer(modifier = Modifier.height(10.dp))
//    Text(text = "version ${LocalParentData.current.version}")
//    Spacer(modifier = Modifier.height(10.dp))
//    CompositionLocalProvider(LocalParentData provides ParentData(2)) {
//        Text(text = "version ${LocalParentData.current.version}")
//    }
}

@Stable
data class ParentData(var version: String) {

    var name by mutableStateOf("default")
}

//以Local开头,这里的值为默认值
//val LocalParentData = compositionLocalOf { ParentData("") }
val LocalParentData = staticCompositionLocalOf { ParentData("") }
