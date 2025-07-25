package net.llvg.thunder.legacy.network

import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import net.llvg.thunder.legacy.event.AbstractEventListener
import net.llvg.thunder.legacy.event.CancelContext
import net.llvg.thunder.legacy.event.asContext
import net.llvg.thunder.legacy.internal.Internal
import net.llvg.utilities.asTyped
import net.llvg.utilities.jClass
import net.minecraft.network.Packet

class PacketListener<P : Packet<*>, E : PacketEvent>
private constructor(
    type: Class<out E>,
    private val pType: Class<out P>,
    block: Boolean = false,
    context: CoroutineContext,
    private val action: suspend context(CancelContext) (P) -> Unit,
) : AbstractEventListener<E>(type, block, context) {
    override fun accept(event: E, collector: Consumer<suspend () -> Unit>) {
        val packetTyped = event.asTyped(pType)
        packetTyped ?: return
        collector.accept {
            try {
                event.asContext.run { action(packetTyped) }
            } catch (e: Throwable) {
                Internal.logger.warn("Failure occur while activating packet-listener", e)
            }
        }
    }
    
    companion object {
        @JvmSynthetic
        @PublishedApi
        internal fun <P : Packet<*>> ofIn(
            pType: Class<out P>,
            block: Boolean,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: suspend context(CancelContext) (P) -> Unit,
        ) = PacketListener(jClass(), pType, block, context, consumer)
        
        @JvmStatic
        @JvmOverloads
        @Suppress("UNUSED")
        fun <P : Packet<*>> ofIn(
            pType: Class<out P>,
            block: Boolean = false,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: BiConsumer<CancelContext, P>,
        ) = PacketListener(jClass(), pType, block, context, consumer::accept)
        
        @JvmSynthetic
        @PublishedApi
        internal fun <P : Packet<*>> ofOut(
            pType: Class<out P>,
            block: Boolean,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: suspend context(CancelContext) (P) -> Unit,
        ) = PacketListener(jClass(), pType, block, context, consumer)
        
        @JvmStatic
        @JvmOverloads
        @Suppress("UNUSED")
        fun <P : Packet<*>> ofOut(
            pType: Class<out P>,
            block: Boolean = false,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: BiConsumer<CancelContext, P>,
        ) = PacketListener(jClass(), pType, block, context, consumer::accept)
    }
}