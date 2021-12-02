package com.yly.learncompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author    yiliyang
 * @date      2021/8/13 下午5:23
 * @version   1.0
 * @since     1.0
 */
class HelloViewModel : ViewModel() {

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData

    fun changeText(name: String) {
        _liveData.postValue(name)
    }
}