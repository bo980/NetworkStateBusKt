package com.liang.network

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.liang.network.core.NetworkBus
import com.liang.network.core.State

fun Application.registerNetworkStateBus() {
    NetworkBus.instance.register(this)
}

fun LifecycleOwner.networkStateObserver(action: (state: State) -> Unit) {
    NetworkBus.instance.with(this).apply {
        observer = Observer {
            action(it)
        }
    }
}