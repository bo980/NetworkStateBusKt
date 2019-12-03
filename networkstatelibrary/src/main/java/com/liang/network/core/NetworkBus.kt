package com.liang.network.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.IntentFilter
import android.net.NetworkRequest
import android.os.Build
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.liang.network.utils.getConnectivityManager
import com.liang.network.utils.put

class NetworkBus private constructor() {

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkBus()
        }
    }

    private val networkStateLiveData: MutableLiveData<State> by lazy {
        MutableLiveData<State>()
    }

    private val stateBusPools: SparseArray<IBus> by lazy {
        SparseArray<IBus>()
    }

    private var version = -1

    fun post(state: State) {
        networkStateLiveData.postValue(state)
    }

    init {
        networkStateLiveData.observeForever {
            version++
            stateBusPools.forEach { _, v ->
                if (version > v.getVersion()) {
                    v.postState(it)
                }
            }
        }
    }

    fun with(lifecycleOwner: LifecycleOwner): IBus {
        return stateBusPools.put(NetStateBus().apply {
            bindLifecycle(lifecycleOwner.lifecycle)
            networkStateLiveData.value?.let {
                if (version > getVersion()) {
                    postState(it)
                }
            }
        })
    }

    fun removeStateBus(bus: IBus) {
        stateBusPools.remove(bus.tag)
    }

    @SuppressLint("MissingPermission")
    fun register(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networkCallback = NetworkCallbackImp()
            val mgr = application.getConnectivityManager()
            mgr?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.registerDefaultNetworkCallback(networkCallback)
                } else {
                    val request = NetworkRequest.Builder().build()
                    it.registerNetworkCallback(request, networkCallback)
                }
            }
        } else {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Constants.ANDROID_NET_CHANGE_ACTION)
            application.registerReceiver(NetworkStateReceiver(), intentFilter)
        }
    }
}

