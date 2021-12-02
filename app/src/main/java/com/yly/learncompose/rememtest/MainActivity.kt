package com.yly.learncompose.rememtest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yly.learncompose.state.CalculatorViewModel
import kotlinx.coroutines.flow.flow

/**
 * @author    yiliyang
 * @date      2021/10/8 上午10:10
 * @version   1.0
 * @since     1.0
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            Content()
//            SnapShots()
            States()
        }
    }

}

@Composable
fun States() {
    val viewModels = viewModel(CalculatorViewModel::class.java)
    Text(text = "${viewModels.result}")
}

@Composable
fun SnapShots() {
    var state by remember {
        mutableStateOf("default")
    }
    val unregisterApplyObserver = Snapshot.registerApplyObserver { changed, _ ->
        println("changed $changed")
    }
    val snapshot1 = Snapshot.takeSnapshot {
        println("takeSnapshot  $it")
    }
    snapshot1.enter {
        state
    }
    snapshot1.dispose()
    state = "changed"
    unregisterApplyObserver.dispose()
}

@Composable
fun Content() {
    var state = remember {
        mutableStateOf("default")
    }

    val stateList = remember {
        mutableStateListOf(1, 2, 3)
    }

    var mutableState by remember {
        mutableStateOf("")
    }

    Column {
        Button(onClick = { stateList.add(4) }) {
            Text(text = stateList.joinToString {
                it.toString()
            })
        }
        Button(onClick = { state.value = "changed" }) {
            Text(text = state.value)
        }
    }
}





















