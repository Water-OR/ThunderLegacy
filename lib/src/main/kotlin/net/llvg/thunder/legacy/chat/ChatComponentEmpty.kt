package net.llvg.thunder.legacy.chat

import net.minecraft.util.ChatComponentStyle
import net.minecraft.util.IChatComponent

class ChatComponentEmpty : IChatComponent, ChatComponentStyle() {
    override fun getUnformattedTextForChat(): String = ""
    
    override fun createCopy() = ChatComponentEmpty().also {
        it.chatStyle = chatStyle.createShallowCopy()
        for (c in siblings) it.appendSibling(c.createCopy())
    }
    
    override fun equals(other: Any?): Boolean =
        if (this === other) true else when (other) {
            is ChatComponentEmpty ->
                super<ChatComponentStyle>.equals(other)
            
            is IChatComponent     ->
                other.unformattedTextForChat.isEmpty() &&
                super<ChatComponentStyle>.equals(other)
            
            else                  -> false
        }
    
    override fun hashCode(): Int = super<ChatComponentStyle>.hashCode()
}