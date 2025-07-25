package net.llvg.thunder.legacy.chat

import java.lang.AutoCloseable
import java.util.function.Consumer
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent

interface ChatComponentBuildScope : AutoCloseable {
    val last: IChatComponent
    
    fun text(value: IChatComponent)
    
    fun style(value: ChatStyle): Consumer<IChatComponent>
    
    fun color(value: EnumChatFormatting): Consumer<IChatComponent>
    
    fun bold(value: Boolean): Consumer<IChatComponent>
    
    fun italic(value: Boolean): Consumer<IChatComponent>
    
    fun obfuscated(value: Boolean): Consumer<IChatComponent>
    
    fun underlined(value: Boolean): Consumer<IChatComponent>
    
    fun strikethrough(value: Boolean): Consumer<IChatComponent>
    
    override fun close()
}
