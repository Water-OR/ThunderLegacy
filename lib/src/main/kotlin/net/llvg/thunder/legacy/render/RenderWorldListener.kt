package net.llvg.thunder.legacy.render

import java.util.function.Consumer
import kotlin.coroutines.EmptyCoroutineContext
import net.llvg.thunder.legacy.event.AbstractEventListener
import net.llvg.thunder.legacy.internal.Internal
import net.llvg.utilities.jClass

class RenderWorldListener
private constructor(
    private val action: suspend (RenderWorldEvent) -> Unit
) : AbstractEventListener<RenderWorldEvent>(jClass(), true, EmptyCoroutineContext) {
    override fun accept(event: RenderWorldEvent, collector: Consumer<suspend () -> Unit>) {
        collector.accept {
            try {
                action(event)
            } catch (e: Throwable) {
                Internal.logger.warn("Failure occur while activating render-world-listener", e)
            }
        }
    }
    
    companion object {
        @JvmSynthetic
        @PublishedApi
        internal fun of(
            action: suspend (RenderWorldEvent) -> Unit
        ) = RenderWorldListener(action)
        
        @JvmStatic
        fun of(
            action: Consumer<RenderWorldEvent>
        ) = RenderWorldListener(action::accept)
    }
}