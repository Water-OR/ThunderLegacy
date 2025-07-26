package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event
import net.minecraft.client.multiplayer.WorldClient

sealed interface WorldChangeEvent : Event {
    val oldWorld: WorldClient?
    
    val newWorld: WorldClient?
    
    companion object {
        private class Impl(
            override val oldWorld: WorldClient?,
            override val newWorld: WorldClient?,
        ) : WorldChangeEvent
        
        internal fun of(
            oldWorld: WorldClient?,
            newWorld: WorldClient?,
        ): WorldChangeEvent = Impl(oldWorld, newWorld)
    }
}