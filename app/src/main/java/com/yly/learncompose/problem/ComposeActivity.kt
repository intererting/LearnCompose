package com.yly.learncompose.problem

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

/**
 * @author    yiliyang
 * @date      2022/7/15 上午9:31
 * @version   1.0
 * @since     1.0
 */
class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StableContent()
        }
    }

}

@Composable
private fun StableContent() {
//    val myData = remember {
//        MyData("yuliyang")
//    }
    val myData = MyData("yuliyang")
    var flag by remember {
        mutableStateOf(false)
    }
    Column {
        Text(text = flag.toString())
        MyDataContent(myData = myData)
        Button(onClick = {
            flag = !flag
//            myData.data = "changed ${System.currentTimeMillis()}"
        }) {
            Text(text = "click")
        }
    }
}

@Composable
private fun MyDataContent(myData: MyData) {
    println("MyDataContent compose ${myData}")
    Text(text = myData.data)
}


/**
 * 如果用var,那么就不是一个Stable的对象,每次都会触发recompose,用val就是Stable的
 *
 * 如果手动添加@Stable,那么就会使用Equal方法比较是否有改变
 * 对于equals方法调用有限制,1：如果使用了remember,那么始终是一个对象，那么equals方法应该返回true，且不会Recompense
 * 2：如果没有使用remember，那么重写equals返回false就会Recompense，返回true就不会
 */
@Stable
class MyData(var data: String) {

    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
        println("equal 调用")
        return false
    }

    override fun hashCode(): Int {
        println("hashcode 调用")
        return data.hashCode()
    }
}
