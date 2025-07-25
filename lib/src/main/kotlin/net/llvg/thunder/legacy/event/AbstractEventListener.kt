package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.llvg.utilities.asTyped

abstract class AbstractEventListener<E : Event>(
    protected val type: Class<out E>,
    protected val block: Boolean,
    protected val context: CoroutineContext,
) : EventListener.Async, EventListener.Block {
    context(s: CoroutineScope)
    final override fun get(event: Event, collector: Consumer<Job>) {
        val eventTyped = event.asTyped(type)
        accept(eventTyped ?: return) {
            collector.accept(s.launch(context) { it() })
        }
    }
    
    final override fun get(event: Event, collector: Consumer<suspend () -> Unit>) {
        val eventTyped = event.asTyped(type)
        accept(eventTyped ?: return) {
            collector.accept { it() }
        }
    }
    
    protected abstract fun accept(event: E, collector: Consumer<suspend () -> Unit>)
    
    override fun register() {
        if (block) {
            EventManager.registerBlock(type, this)
        } else {
            EventManager.registerAsync(type, this)
        }
    }
}