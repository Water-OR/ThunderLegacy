package net.llvg.thunder.legacy.event

import java.util.function.BooleanSupplier
import java.util.function.Consumer
import net.llvg.thunder.legacy.util.TrueSupplier
import net.llvg.utilities.asTyped

abstract class AbstractEventListener<E : Event>(
    protected val type: Class<out E>,
) : EventListener {
    protected var activeSupplier = TrueSupplier.of()
    
    final override fun get(event: Event, collector: Consumer<Runnable>) {
        if (activeSupplier.asBoolean) accept(event.asTyped(type) ?: return, collector)
    }
    
    protected abstract fun accept(event: E, collector: Consumer<Runnable>)
    
    fun register() = EventManager.register(type, this)
    
    @Suppress("UNUSED")
    fun withActiveSupplier(value: BooleanSupplier) = apply {
        this.activeSupplier = value
    }
}