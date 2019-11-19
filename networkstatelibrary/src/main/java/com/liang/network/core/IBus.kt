package com.liang.network.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer

abstract class IBus {
    protected var lastVersion = -1
    var enable = false
    val tag: Int by lazy {
        init()
    }

    var observer: Observer<State>? = null

    abstract fun init(): Int
    abstract fun bindLifecycle(lifecycle: Lifecycle)
    abstract fun unBindLifecycle(unit: () -> Unit)
    abstract fun postState(state: State)

    abstract fun getVersion(): Int
}