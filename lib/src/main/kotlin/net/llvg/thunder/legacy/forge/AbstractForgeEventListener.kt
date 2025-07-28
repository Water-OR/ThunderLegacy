package net.llvg.thunder.legacy.forge

import java.util.function.BooleanSupplier
import java.util.function.Consumer
import net.llvg.thunder.legacy.util.TrueSupplier
import net.llvg.utilities.asTyped

abstract class AbstractForgeEventListener<E : ForgeEvent>(
    protected val type: Class<out E>
) : ForgeEventListener {
    protected var activeSupplier = TrueSupplier.of()
    
    protected abstract fun accept(event: E, collector: Consumer<Runnable>)
    
    final override fun get(event: ForgeEvent, collector: Consumer<Runnable>) {
        if (activeSupplier.asBoolean) accept(event.asTyped(type) ?: return, collector)
    }
    
    fun register() = ForgeEventManager.register(type, this)
    
    @Suppress("UNUSED")
    fun withActiveSupplier(value: BooleanSupplier) = apply {
        this.activeSupplier = value
    }
}