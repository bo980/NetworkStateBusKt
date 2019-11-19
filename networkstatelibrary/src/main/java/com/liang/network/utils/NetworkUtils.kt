package com.liang.network.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.SparseArray
import com.liang.network.core.IBus
import com.liang.network.core.State

fun Context.getConnectivityManager(): ConnectivityManager? {
    return applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
}

//判断网络是否可用
@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    val mgr = getConnectivityManager() ?: return false
    val networkIfs = mgr.allNetworkInfo
    for (info in networkIfs) {
        if (info.state == NetworkInfo.State.CONNECTED) {
            return true
        }
    }
    return false
}

//WiFi是否连接
@SuppressLint("MissingPermission")
fun Context.isWifiConnected(): Boolean {
    val mgr = getConnectivityManager() ?: return false
    val wifiNetworkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    return wifiNetworkInfo?.isConnected ?: false
}

//移动网络是否连接
@SuppressLint("MissingPermission")
fun Context.isMobileConnected(): Boolean {
    val mgr = getConnectivityManager() ?: return false
    val mobileNetworkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    return mobileNetworkInfo?.isConnected ?: false
}

@SuppressLint("MissingPermission")
fun Context.getNetState(): State {
    val mgr = getConnectivityManager() ?: return State.NONE
    val networkInfo = mgr.activeNetworkInfo ?: return State.NONE

    val netType = networkInfo.type

    if (netType == ConnectivityManager.TYPE_MOBILE) {
        return State.MOBILE
    } else if (netType == ConnectivityManager.TYPE_WIFI) {
        return State.WIFI
    }
    return State.NONE
}

fun SparseArray<IBus>.put(bus: IBus) : IBus {
    put(bus.tag, bus)
    return bus
}