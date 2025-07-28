package net.llvg.thunder.legacy.network

import java.util.function.BiConsumer
import java.util.function.Consumer
import net.llvg.thunder.legacy.event.CancelContext
import net.minecraft.network.Packet

class PacketEventListenerDefault<P : Packet<*>>
private constructor(
    type: Class<out P>,
    flow: Boolean,
    private val action: BiConsumer<CancelContext, P>
) : AbstractPacketEventListener<P>(type, flow) {
    context(c: CancelContext)
    override fun accept(packet: P, collector: Consumer<Runnable>) =
        collector.accept { action.accept(c, packet) }
    
    companion object {
        @JvmStatic
        fun <P : Packet<*>> ofIn(
            clazz: Class<out P>,
            action: BiConsumer<CancelContext, P>,
        ) = PacketEventListenerDefault(clazz, true, action)
        
        @JvmStatic
        fun <P : Packet<*>> ofOut(
            clazz: Class<out P>,
            action: BiConsumer<CancelContext, P>,
        ) = PacketEventListenerDefault(clazz, false, action)
    }
}