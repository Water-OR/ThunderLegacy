package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import net.llvg.utilities.asTyped

abstract class AbstractEventListener<E : Event>(
    protected val type: Class<out E>
) : EventListener {
    final override fun get(event: Event, collector: Consumer<Runnable>) {
        accept(event.asTyped(type) ?: return, collector)
    }
    
    protected abstract fun accept(event: E, collector: Consumer<Runnable>)
    
    fun register() = EventManager.register(type, this)
}