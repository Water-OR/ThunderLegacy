package net.llvg.thunder.legacy.event

import java.util.function.Consumer

class EventListenerDefault<E : Event>
private constructor(
    type: Class<out E>,
    private val action: Consumer<E>,
) : AbstractEventListener<E>(type) {
    override fun accept(event: E, collector: Consumer<Runnable>) =
        collector.accept { action.accept(event) }
    
    companion object {
        @JvmStatic
        fun <E : Event> of(
            clazz: Class<out E>,
            consumer: Consumer<E>,
        ) = EventListenerDefault(clazz, consumer)
    }
}