package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

sealed interface EventListener {
    interface Async : EventListener {
        context(scope: CoroutineScope)
        operator fun get(event: Event, collector: Consumer<Job>)
    }
    
    interface Block : EventListener {
        operator fun get(event: Event, collector: Consumer<suspend () -> Unit>)
    }
    
    fun register()
}