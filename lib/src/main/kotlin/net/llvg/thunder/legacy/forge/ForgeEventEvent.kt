package net.llvg.thunder.legacy.forge

import net.llvg.thunder.legacy.event.Event

sealed interface ForgeEventEvent : Event {
    val event: ForgeEvent
    
    companion object {
        private class Impl(override val event: ForgeEvent) : ForgeEventEvent
        
        internal fun of(event: ForgeEvent): ForgeEventEvent = Impl(event)
    }
}