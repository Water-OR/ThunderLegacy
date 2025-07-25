package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event
import net.minecraft.client.Minecraft

sealed interface MinecraftInstantiationEvent : Event {
    val mc: Minecraft
    
    companion object {
        private class Impl(override val mc: Minecraft) : MinecraftInstantiationEvent
        
        internal fun of(mc: Minecraft): MinecraftInstantiationEvent = Impl(mc)
    }
}