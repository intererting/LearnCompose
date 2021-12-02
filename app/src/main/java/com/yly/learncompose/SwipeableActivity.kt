package com.yly.learncompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author    yiliyang
 * @date      2021/9/17 上午8:50
 * @version   1.0
 * @since     1.0
 */
class SwipeableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeableSample()
        }
    }
}