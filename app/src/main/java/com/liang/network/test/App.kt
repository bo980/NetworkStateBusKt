package com.liang.network.test

import android.app.Application
import com.liang.network.registerNetworkStateBus

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        registerNetworkStateBus()
    }
}