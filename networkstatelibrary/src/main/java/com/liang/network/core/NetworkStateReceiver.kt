package com.liang.network.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.liang.network.utils.getNetState

class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.action?.let {
            when (it) {
                Constants.ANDROID_NET_CHANGE_ACTION -> {
                    NetworkBus.instance.post(context.getNetState())
                }
            }
        }

    }

}