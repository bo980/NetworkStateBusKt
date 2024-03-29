package com.liang.network.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.liang.network.networkStateObserver

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        lifecycle.addObserver(TestObserver())

        networkStateObserver {
            Log.e("MainActivity", "observer...$it")
        }
    }
}
