package com.liang.network.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NetStateBus : IBus(), LifecycleObserver {

    private var version = lastVersion

    @Volatile
    private var mState: State = State.NONE

    override fun postState(state: State) {
        mState = state
        version++
        onChange()
    }

    override fun init(): Int {
        return System.nanoTime().toInt()
    }

    private var mLifecycle: Lifecycle? = null

    override fun getVersion(): Int {
        return lastVersion
    }

    override fun bindLifecycle(lifecycle: Lifecycle) {
        mLifecycle = lifecycle.apply {
            addObserver(this@NetStateBus)
        }
    }

    override fun unBindLifecycle(unit: () -> Unit) {
        mLifecycle?.removeObserver(this)
        unit.invoke()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        enable = true
        onChange()
    }

    private fun onChange() {
        if (enable && version > lastVersion) {
            observer?.onChanged(mState)
            lastVersion = version
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        enable = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unBindLifecycle {
            NetworkBus.instance.removeStateBus(this)
        }
    }

}