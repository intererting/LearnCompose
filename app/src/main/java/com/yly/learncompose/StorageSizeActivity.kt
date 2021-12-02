package com.yly.learncompose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * @author    yiliyang
 * @date      2021/8/30 上午11:28
 * @version   1.0
 * @since     1.0
 */
const val NUM_BYTES_NEEDED_FOR_MY_APP = 1024 * 1024 * 10L;

class StorageSizeActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        findViewById<Button>(R.id.test).setOnClickListener {

            val file = File(filesDir, "yuliyang.txt")
            if (!file.exists()) {
                file.createNewFile()
            }

            val storageManager = applicationContext.getSystemService<StorageManager>()!!
            val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
            val availableBytes: Long =
                storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
            if (availableBytes >= NUM_BYTES_NEEDED_FOR_MY_APP) {
                storageManager.allocateBytes(
                    FileOutputStream(file).fd, NUM_BYTES_NEEDED_FOR_MY_APP
                )
            } else {
                val storageIntent = Intent().apply {
                    action = ACTION_MANAGE_STORAGE
                }
                // Display prompt to user, requesting that they choose files to remove.
            }
        }
    }
}