package com.yly.learncompose.problem

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.yly.learncompose.ui.theme.LearnComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

/**
 * @author    yiliyang
 * @date      2021/8/31 下午4:06
 * @version   1.0
 * @since     1.0
 */
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                val state: MainState by viewModel.state.collectAsState()
                MainScaffold(
                    state,
                    onValueUpdate = { viewModel.updateSlider(it.roundToInt()) },
                    //Recompose添加，参数stable而且equals方法不相等
                    //这个地方使用方法引用，可以防止CounterRow由于equals方法不相等导致的recompose
//                    onButtonClick = viewModel::updateCounter
                    onButtonClick = { viewModel::updateCounter }
                )
            }
        }
    }
}

@Composable
fun MainScaffold(state: MainState, onValueUpdate: (Float) -> Unit, onButtonClick: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                value = state.sliderValue.toFloat(),
                onValueChange = onValueUpdate,
                valueRange = 0f..10f,
                steps = 10
            )
//            CounterRow(counter = state.counter, onButtonClick = onButtonClick)
            //key不管作用，onButtonClick其实还是可变的
            key(state.counter) {
                CounterRow(counter = state.counter, onButtonClick = onButtonClick)
            }
        }
    }
}

@Composable
fun CounterRow(counter: Int, onButtonClick: () -> Unit) {
    /** SHOULD NOT BE CALLED ON SLIDER CHANGE **/
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = onButtonClick) {
            Text(text = "Click me!")
        }
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = counter.toString())
    }
}

data class MainState(
    val sliderValue: Int = 0,
    val counter: Int = 0
)

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState())

    val state: StateFlow<MainState>
        get() = _state.asStateFlow()

    fun updateCounter() {
        _state.value = state.value.copy(counter = state.value.counter.plus(1))
    }

    fun updateSlider(value: Int) {
        _state.value = state.value.copy(sliderValue = value)
    }
}