package net.llvg.thunder.legacy.chat

import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import net.llvg.thunder.legacy.event.AbstractEventListener
import net.llvg.thunder.legacy.internal.Internal
import net.llvg.utilities.jClass
import net.minecraft.util.IChatComponent

sealed class ChatListener(
    private val regex: Regex,
    block: Boolean,
    context: CoroutineContext,
    private val action: suspend (MatchResult) -> Unit,
) : AbstractEventListener<ChatEvent>(jClass(), block, context) {
    override fun accept(event: ChatEvent, collector: Consumer<suspend () -> Unit>) {
        val match = regex.find(textToString(event.text))
        match ?: return
        collector.accept {
            try {
                action(match)
            } catch (e: Throwable) {
                Internal.logger.warn("Failure occur while activating chat-listener", e)
            }
        }
    }
    
    protected abstract fun textToString(text: IChatComponent): String
    
    private class Formatted(
        regex: Regex,
        block: Boolean,
        context: CoroutineContext,
        consumer: suspend (MatchResult) -> Unit,
    ) : ChatListener(regex, block, context, consumer) {
        override fun textToString(text: IChatComponent): String =
            text.formattedText
    }
    
    private class Unformatted(
        regex: Regex,
        block: Boolean,
        context: CoroutineContext,
        consumer: suspend (MatchResult) -> Unit,
    ) : ChatListener(regex, block, context, consumer) {
        override fun textToString(text: IChatComponent): String =
            text.unformattedText
    }
    
    companion object {
        @JvmSynthetic
        @PublishedApi
        internal fun ofFormatted(
            regex: Regex,
            block: Boolean,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: suspend (MatchResult) -> Unit,
        ): ChatListener = Formatted(regex, block, context, consumer)
        
        @JvmStatic
        @JvmOverloads
        @Suppress("UNUSED")
        fun ofFormatted(
            pattern: Pattern,
            block: Boolean = false,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Formatted(pattern.toRegex(), block, context, consumer::accept)
        
        @JvmSynthetic
        @PublishedApi
        internal fun ofUnformatted(
            regex: Regex,
            block: Boolean,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: suspend (MatchResult) -> Unit,
        ): ChatListener = Unformatted(regex, block, context, consumer)
        
        @JvmStatic
        @JvmOverloads
        @Suppress("UNUSED")
        fun ofUnformatted(
            pattern: Pattern,
            block: Boolean = false,
            context: CoroutineContext = EmptyCoroutineContext,
            consumer: Consumer<MatchResult>,
        ): ChatListener = Unformatted(pattern.toRegex(), block, context, consumer::accept)
    }
}