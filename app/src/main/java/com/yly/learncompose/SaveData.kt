package com.yly.learncompose

import android.os.Parcelable
import androidx.compose.runtime.saveable.mapSaver
import kotlinx.parcelize.Parcelize

/**
 * @author    yiliyang
 * @date      2021/8/13 下午5:49
 * @version   1.0
 * @since     1.0
 */
@Parcelize
data class SaveData(val id: String) : Parcelable {


}


