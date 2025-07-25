package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("UNUSED")
class EventListenerDefault<E : Event>
private constructor(
    type: Class<out E>,
    block: Boolean,
    context: CoroutineContext,
    private val action: suspend (E) -> Unit,
) : EventListener, AbstractEventListener<E>(type, block, context) {
    override fun accept(event: E, collector: Consumer<suspend () -> Unit>) {
        collector.accept { action(event) }
    }
    
    companion object {
        @JvmSynthetic
        @PublishedApi
        internal fun <E : Event> of(
            clazz: Class<out E>,
            block: Boolean,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: suspend (E) -> Unit,
        ) = EventListenerDefault(clazz, block, context, consumer)
        
        @JvmStatic
        @JvmOverloads
        fun <E : Event> of(
            clazz: Class<out E>,
            block: Boolean = false,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: Consumer<E>,
        ) = EventListenerDefault(clazz, block, context, consumer::accept)
    }
}