package net.llvg.thunder.legacy.vanilla

import net.llvg.thunder.legacy.event.Event

sealed interface GameLoopEvent : Event {
    companion object {
        private object Impl : GameLoopEvent
        
        internal fun of(): GameLoopEvent = Impl
    }
}