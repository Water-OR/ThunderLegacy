package net.llvg.thunder.legacy.network

import net.llvg.thunder.legacy.event.Event
import net.minecraft.network.Packet

sealed interface PacketEvent : Event.Cancellable {
    val packet: Packet<*>
    
    sealed interface Outbound : PacketEvent {
        companion object {
            private class Impl(override val packet: Packet<*>) : Outbound, Event.Cancellable.Default()
            
            internal fun of(packet: Packet<*>): Outbound = Impl(packet)
        }
    }
    
    sealed interface Inbound : PacketEvent {
        companion object {
            private class Impl(override val packet: Packet<*>) : Inbound, Event.Cancellable.Default()
            
            internal fun of(packet: Packet<*>): Inbound = Impl(packet)
        }
    }
}