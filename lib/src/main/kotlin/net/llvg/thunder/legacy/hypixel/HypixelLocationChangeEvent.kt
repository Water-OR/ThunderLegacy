package net.llvg.thunder.legacy.hypixel

import net.llvg.thunder.legacy.event.Event

sealed interface HypixelLocationChangeEvent : Event {
    val oldLocation: HypixelLocation?
    
    val newLocation: HypixelLocation?
    
    companion object {
        private class Impl(
            override val oldLocation: HypixelLocation?,
            override val newLocation: HypixelLocation?,
        ) : HypixelLocationChangeEvent
        
        internal fun of(
            oldLocation: HypixelLocation?,
            newLocation: HypixelLocation?,
        ): HypixelLocationChangeEvent = Impl(oldLocation, newLocation)
    }
}