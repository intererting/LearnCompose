package com.yly.learncompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * @author    yiliyang
 * @date      2021/8/26 下午5:14
 * @version   1.0
 * @since     1.0
 */
class GestureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            GestureClick()
//            NestedScroll()
//            SelfNestedScroll()
            SwipeableSample()
//            TransformableSample()
        }
    }
}

@Composable
fun TransformableSample() {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(
        Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .background(Color.Blue)
            .fillMaxSize()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(1)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}

@Composable
fun SelfNestedScroll() {
    Column {
        repeat(5) {
            Box(modifier = Modifier.height(100.dp)) {
                Text(text = "$it", textAlign = TextAlign.Center, modifier = Modifier.height(150.dp))
            }
        }
    }
}

@Composable
fun NestedScroll() {
    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GestureClick() {

    val scrollableState = rememberScrollableState() {
        println(it)
        it
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .scrollable(
                scrollableState, orientation = Orientation.Vertical
            )
    ) {
        Text(text = "click", modifier = Modifier.clickable {
            println("click")
        })

//        Text(text = "click", modifier = Modifier.pointerInput(Unit) {
//            detectTapGestures {
//                println(it.getDistance())
//                println(it.y)
//            }
//        })

//        val state = rememberScrollState()
//
//        LaunchedEffect(key1 = Unit, block = {
//            state.animateScrollTo(200)
//        })
//
//        Column(modifier = Modifier.verticalScroll(state)) {
//            repeat(20) {
//                Text(text = "item $it", modifier = Modifier.height(60.dp))
//            }
//        }


    }
}


















