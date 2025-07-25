package net.llvg.thunder.legacy.chat

import net.llvg.thunder.legacy.event.Event
import net.minecraft.util.IChatComponent

sealed interface ChatEvent : Event {
    val text: IChatComponent
    
    companion object {
        private class Impl(override val text: IChatComponent) : ChatEvent
        
        internal fun of(text: IChatComponent): ChatEvent = Impl(text)
    }
}