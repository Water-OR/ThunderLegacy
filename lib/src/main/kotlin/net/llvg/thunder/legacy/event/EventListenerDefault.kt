package net.llvg.thunder.legacy.event

import java.util.function.Consumer

@Suppress("UNUSED")
class EventListenerDefault<E : Event>
private constructor(
    type: Class<out E>,
    private val action: Consumer<E>,
) : Consumer<Event>, AbstractEventListener<E>(type) {
    override fun run(event: E) = action.accept(event)
    
    companion object {
        @JvmStatic
        fun <E : Event> of(
            clazz: Class<out E>,
            consumer: Consumer<E>,
        ) = EventListenerDefault(clazz, consumer)
    }
}