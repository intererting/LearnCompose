package com.yly.learncompose

import android.os.Bundle
import android.widget.Space
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author    yiliyang
 * @date      2021/8/16 上午10:41
 * @version   1.0
 * @since     1.0
 */
class SideEffectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            TestKey()
//            TestLaunchedEffect()
//            TestRememberCoroutineScope()
//            TestRememberUpdatedState()
//            TestDisposableEffect()
//            TestSideEffect()
//            TestProduceState()
//            TestDerivedStateOf()
            TestSnapshotFlow()
        }
    }

}

/**
 * 证明一下key是否是通过equals刷新
 *
 * 结论：key是是通过equals刷新的
 */
@Composable
fun TestKey() {
    var key by remember {
        mutableStateOf(KeyEquals("1"))
    }
//    val state = remember(key1 = key) {
//        System.currentTimeMillis()
//    }
    val state = remember(key1 = ":") {
        System.currentTimeMillis()
    }
    Column {
        Text(text = "$state")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            key = KeyEquals("2")
        }) {
            Text(text = "change")
        }
    }
}

data class KeyEquals(val key: String) {
    override fun equals(other: Any?): Boolean {
        return false
    }
}


@Composable
fun TestSnapshotFlow() {

    var state by remember {
        mutableStateOf("default")
    }

    //snapshotFlow不受LaunchedEffect key影响 DerivedStateOf一样
    LaunchedEffect(key1 = "default", block = {
        snapshotFlow {
            state
        }.map {
            "$it ----》 changed"
        }.collect {
            println(it)
        }
    })
    Button(onClick = {
        state = "default 1"
    }) {
        Text(text = "change")
    }
}

@Composable
fun TestDerivedStateOf() {
    var baseState by remember {
        mutableStateOf(1)
    }

//    val addState by derivedStateOf {
//        "addState $baseState"
//    }

    var keyState by remember {
        mutableStateOf("key")
    }

//    val addState by remember(System.currentTimeMillis()) {
    val addState by remember(keyState) {
        //目前测试来看不受key约束,就算是key没变,也会刷新
//        derivedStateOf {
//            "newState $baseState ${System.currentTimeMillis()}"
//        }
        mutableStateOf("addState ${System.currentTimeMillis()}")
    }

    Column {
        Text(text = "    $baseState")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "    $addState")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            baseState += 1
//            keyState = "change key"
        }) {
            Text(text = "++")
        }
    }

}

@Composable
fun TestProduceState() {
    var key by remember {
        mutableStateOf("default")
    }
    val state = getProduceState(key = key)
    Column {
        Button(onClick = {
            key = "changed"
        }) {
            Text(text = "change key")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = state.value)
    }
}

@Composable
fun getProduceState(key: String): State<String> {
    //和launchedEffect一样,这个地方key发生变化,会取消当前协程,然后重新开始一个协程
    return produceState(initialValue = "default", key1 = key) {
        //协程
        delay(5000)
        println("生成state")
        //设置value
        value = "return $key ${System.currentTimeMillis()}"

        awaitDispose {
            //可用于取消监听
            println("awaitDispose")
        }
    }
}

@Composable
fun TestSideEffect() {

    var person by remember {
        mutableStateOf(Person("yuliyang"))
    }

    Column {
        Text(text = person.name, modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            person = Person("changed")
        }) {
            Text(text = "change person")
        }
    }

    SideEffect {
        //每一次recompose后才会调用,所以这里设置值没用
        println("sideEffect")
        //unused
        person.name = "change by sideeffect"
    }

}

data class Person(var name: String)


@Composable
fun TestDisposableEffect() {
    var data by remember {
        mutableStateOf("default")
    }

    Button(onClick = {
        data = "changed"
    }) {
        Text(text = "change")
    }

    //可以捕获生命周期结束,不同的LaunchedEffect是不行的,但是这不是协程
    DisposableEffect(key1 = data) {
        println(data)
        onDispose {
            println("onDispose")
        }
    }
}

@Composable
fun TestLaunchedEffect() {
    TimerScreen()
}

@Composable
fun TestRememberUpdatedState() {
    var lambdaFunc by remember {
        mutableStateOf({ println("lambda invoke") })
    }

    println("recompose")
    Button(onClick = {
        lambdaFunc = { println("lambda invoke changed") }
    }) {
        Text(text = "change lambda")
    }
    //是否触发recompose是通过equals方法
    TestLambdaRecomposition(lambdaFunc)
}

@Composable
fun TestLambdaRecomposition(func: () -> Unit) {
    //默认情况下,如果key1和func无关,那么func改变了不会触发LaunchedEffect取消重启
    //但是如果要捕获到最新值可以使用rememberUpdatedState,当func改变时,可以用invokeFunc.value获取最新值
    val invokeFunc = rememberUpdatedState(newValue = func)
    println("recompose")
    LaunchedEffect(key1 = true) {
        delay(5000)
        invokeFunc.value()
    }
}

@Composable
fun TestRememberCoroutineScope() {
    //只要是带有remember的都是可以直接用的
    val myScope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            myScope.launch {
                repeat(1000) {
                    println("协程 1")
                    delay(2000)
                }
            }
        }) {
            Text(text = "开启协程1")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            myScope.launch {
                repeat(1000) {
                    println("协程 2")
                    delay(1000)
                }
            }
        }) {
            Text(text = "开启协程2")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            myScope.cancel()
        }) {
            Text(text = "取消协程")
        }
    }
}

@Composable
fun TimerScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var timerDuration by remember {
            mutableStateOf(1000L) // default value = 1 sec
        }
        Button({
            timerDuration -= 1000
        }) {
            Text("-1 second")
        }
        Text(timerDuration.toString())
        Button({
            timerDuration += 1000
        }) {
            Text("+1 second")
        }
        Timer(timerDuration = timerDuration)
    }
}

@Composable
fun Timer(timerDuration: Long) {
    LaunchedEffect(key1 = timerDuration, block = {
        //在Compose中使用协程
        //1:如果key1发生改变,协程将会cancel
        //2:协程会重启
        try {
//            launch {
            delay(timeMillis = timerDuration)
            println("Timer ended")
//            }
        } catch (ex: Exception) {
            println("timer cancelled")
        }
    })
}