package net.llvg.thunder.legacy.network

import java.util.function.Consumer
import net.llvg.thunder.legacy.event.CancelContext
import net.minecraft.network.Packet

interface PacketEventListener {
    context(c: CancelContext)
    operator fun get(packet: Packet<*>, collector: Consumer<Runnable>)
}