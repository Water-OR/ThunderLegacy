package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event

sealed interface GameShutdownEvent : Event {
    companion object {
        private object Impl : GameShutdownEvent
        
        internal fun of(): GameShutdownEvent = Impl
    }
}