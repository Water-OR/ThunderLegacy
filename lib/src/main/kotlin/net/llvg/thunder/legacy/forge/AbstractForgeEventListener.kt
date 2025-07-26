package net.llvg.thunder.legacy.forge

import java.util.function.Consumer
import net.llvg.utilities.asTyped

abstract class AbstractForgeEventListener<E : ForgeEvent>(
    protected val type: Class<out E>
) : ForgeEventListener {
    final override fun get(event: ForgeEvent, collector: Consumer<Runnable>) {
        accept(event.asTyped(type) ?: return, collector)
    }
    
    protected abstract fun accept(event: E, collector: Consumer<Runnable>)
    
    fun register() = ForgeEventManager.register(type, this)
}