package net.llvg.thunder.legacy.forge

import java.util.function.Consumer

class ForgeEventListenerDefault<E : ForgeEvent>
private constructor(
    type: Class<out E>,
    private val action: Consumer<E>,
) : AbstractForgeEventListener<E>(type) {
    override fun accept(event: E, collector: Consumer<Runnable>) =
        collector.accept { action.accept(event) }
    
    companion object {
        @JvmStatic
        fun <E : ForgeEvent> of(
            clazz: Class<out E>,
            action: Consumer<E>
        ) = ForgeEventListenerDefault(clazz, action)
    }
}