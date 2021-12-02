package com.yly.learncompose.state

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import java.lang.Integer.sum

/**
 * @author    yiliyang
 * @date      2021/10/8 下午2:43
 * @version   1.0
 * @since     1.0
 */
class CalculatorViewModel(
    state: SavedStateHandle,
) : ViewModel() {
    var v1 by state.mutableStateOf(1)
    var v2 by state.mutableStateOf(2)

    val result by state.mutableStateOf(0) { valueLoadedFromState, setter ->
        snapshotFlow { v1 to v2 }
            .drop(if (valueLoadedFromState != null) 1 else 0)
            .mapLatest {
                it.first + it.second
            }
            .onEach {
                setter(it)
            }
            .launchIn(viewModelScope)
    }
}