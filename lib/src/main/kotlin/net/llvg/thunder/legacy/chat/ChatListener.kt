package net.llvg.thunder.legacy.chat

import java.util.function.Consumer
import java.util.regex.Pattern
import net.llvg.thunder.legacy.event.AbstractEventListener
import net.llvg.utilities.jClass
import net.minecraft.util.IChatComponent

sealed class ChatListener(
    private val regex: Regex,
    private val action: Consumer<MatchResult>,
) : AbstractEventListener<ChatEvent>(jClass()) {
    override fun run(event: ChatEvent) {
        action.accept(regex.find(textToString(event.text)) ?: return)
    }
    
    protected abstract fun textToString(text: IChatComponent): String
    
    private class Formatted(
        regex: Regex,
        consumer: Consumer<MatchResult>,
    ) : ChatListener(regex, consumer) {
        override fun textToString(text: IChatComponent): String = text.formattedText
    }
    
    private class Unformatted(
        regex: Regex,
        consumer: Consumer<MatchResult>,
    ) : ChatListener(regex, consumer) {
        override fun textToString(text: IChatComponent): String = text.unformattedText
    }
    
    companion object {
        @JvmSynthetic
        fun ofFormatted(
            pattern: Regex,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Formatted(pattern, consumer)
        
        @JvmStatic
        @Suppress("UNUSED")
        fun ofFormatted(
            pattern: Pattern,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Formatted(pattern.toRegex(), consumer)
        
        @JvmSynthetic
        fun ofUnformatted(
            regex: Regex,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Unformatted(regex, consumer)
        
        @JvmStatic
        @Suppress("UNUSED")
        fun ofUnformatted(
            pattern: Pattern,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Unformatted(pattern.toRegex(), consumer)
    }
}