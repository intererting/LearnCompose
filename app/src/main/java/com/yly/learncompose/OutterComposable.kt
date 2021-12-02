package com.yly.learncompose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

/**
 * @author    yiliyang
 * @date      2021/8/26 下午5:05
 * @version   1.0
 * @since     1.0
 */
object OutterComposable {

    @Composable
    fun HelloScreen() {
        Text(text = "hello OutterComposable")
    }
}