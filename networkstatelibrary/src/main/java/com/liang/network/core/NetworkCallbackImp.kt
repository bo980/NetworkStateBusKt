package com.liang.network.core

import android.annotation.TargetApi
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkCallbackImp : ConnectivityManager.NetworkCallback() {

    private var mNetState: State = State.NONE

    override fun onLost(network: Network) {
        super.onLost(network)
        if (mNetState != State.NONE) {
            mNetState = State.NONE
            NetworkBus.instance.post(mNetState)
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        var netState = State.NONE
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            netState = State.WIFI
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            netState = State.MOBILE
        }

        if (netState != mNetState) {
            mNetState = netState
            NetworkBus.instance.post(mNetState)
        }

    }
}
