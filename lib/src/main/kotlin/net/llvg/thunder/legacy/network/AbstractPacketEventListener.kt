package net.llvg.thunder.legacy.network

import java.util.function.BooleanSupplier
import java.util.function.Consumer
import net.llvg.thunder.legacy.event.CancelContext
import net.llvg.thunder.legacy.util.TrueSupplier
import net.llvg.utilities.asTyped
import net.minecraft.network.Packet

abstract class AbstractPacketEventListener<P : Packet<*>>(
    protected val type: Class<out P>,
    val flow: Boolean,
) : PacketEventListener {
    protected var activeSupplier = TrueSupplier.of()
    
    context(c: CancelContext)
    protected abstract fun accept(packet: P, collector: Consumer<Runnable>)
    
    context(c: CancelContext)
    final override fun get(packet: Packet<*>, collector: Consumer<Runnable>) {
        if (activeSupplier.asBoolean) accept(packet.asTyped(type) ?: return, collector)
    }
    
    fun register() = PacketEventManager.let { if (flow) it.registerIn(type, this) else it.registerOut(type, this) }
    
    @Suppress("UNUSED")
    fun withActiveSupplier(value: BooleanSupplier) = apply {
        this.activeSupplier = value
    }
}