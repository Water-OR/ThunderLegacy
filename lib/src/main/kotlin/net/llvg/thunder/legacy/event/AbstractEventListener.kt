package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import net.llvg.utilities.asTyped

abstract class AbstractEventListener<E : Event>(
    val type: Class<out E>
) : Consumer<Event> {
    final override fun accept(event: Event) {
        run(event.asTyped(type) ?: return)
    }
    
    protected abstract fun run(event: E)
    
    @Synchronized
    fun register() = EventManager.register(type, this)
}