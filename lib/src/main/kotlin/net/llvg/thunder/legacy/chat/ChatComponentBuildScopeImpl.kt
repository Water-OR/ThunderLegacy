package net.llvg.thunder.legacy.chat

import java.util.function.Consumer
import kotlin.internal.InlineOnly
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent

@PublishedApi
internal class ChatComponentBuildScopeImpl(
    private val owner: IChatComponent
) : ChatComponentBuildScope {
    override var last = owner
        private set
        get() {
            checkIsClosed()
            return field
        }
    
    private var closed = false
    
    @InlineOnly
    private inline fun checkIsClosed() {
        if (closed) throw UnsupportedOperationException("Extender is already closed!")
    }
    
    @Synchronized
    override fun text(value: IChatComponent) {
        checkIsClosed()
        owner.appendSibling(value)
        last = value
    }
    
    @Synchronized
    override fun style(value: ChatStyle): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle = value }
    }
    
    @Synchronized
    override fun color(value: EnumChatFormatting): Consumer<IChatComponent> {
        checkIsClosed()
        require(value.isColor) { "[value] is not a color. This is not permitted." }
        return Consumer { it.chatStyle.color = value }
    }
    
    @Synchronized
    override fun bold(value: Boolean): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle.bold = value }
    }
    
    @Synchronized
    override fun italic(value: Boolean): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle.italic = value }
    }
    
    @Synchronized
    override fun obfuscated(value: Boolean): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle.obfuscated = value }
    }
    
    @Synchronized
    override fun underlined(value: Boolean): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle.underlined = value }
    }
    
    @Synchronized
    override fun strikethrough(value: Boolean): Consumer<IChatComponent> {
        checkIsClosed()
        return Consumer { it.chatStyle.strikethrough = value }
    }
    
    @Synchronized
    override fun close() {
        if (closed) return
        closed = true
    }
}