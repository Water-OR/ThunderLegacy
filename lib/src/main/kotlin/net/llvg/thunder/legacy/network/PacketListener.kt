package net.llvg.thunder.legacy.network

import java.util.function.BiConsumer
import java.util.function.Consumer
import net.llvg.thunder.legacy.event.AbstractEventListener
import net.llvg.thunder.legacy.event.CancelContext
import net.llvg.thunder.legacy.event.asContext
import net.llvg.utilities.asTyped
import net.llvg.utilities.jClass
import net.minecraft.network.Packet

class PacketListener<P : Packet<*>, E : PacketEvent>
private constructor(
    type: Class<out E>,
    private val pType: Class<out P>,
    private val action: BiConsumer<CancelContext, P>,
) : AbstractEventListener<E>(type) {
    override fun accept(event: E, collector: Consumer<Runnable>) {
        val packetTyped = event.packet.asTyped(pType)
        packetTyped ?: return
        collector.accept { action.accept(event.asContext, packetTyped) }
    }
    
    companion object {
        @JvmStatic
        fun <P : Packet<*>> ofIn(
            pType: Class<out P>,
            consumer: BiConsumer<CancelContext, P>,
        ) = PacketListener(jClass(), pType, consumer)
        
        @JvmStatic
        fun <P : Packet<*>> ofOut(
            pType: Class<out P>,
            consumer: BiConsumer<CancelContext, P>,
        ) = PacketListener(jClass(), pType, consumer)
    }
}