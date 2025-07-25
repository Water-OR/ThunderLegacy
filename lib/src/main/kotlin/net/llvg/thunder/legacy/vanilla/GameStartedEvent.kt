package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event
import net.minecraft.client.Minecraft

sealed interface GameStartedEvent : Event {
    val mc: Minecraft
    
    companion object {
        private class Impl(override val mc: Minecraft) : GameStartedEvent
        
        internal fun of(mc: Minecraft): GameStartedEvent = Impl(mc)
    }
}