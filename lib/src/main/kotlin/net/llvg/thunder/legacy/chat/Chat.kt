@file:JvmName("ChatUtil")

package net.llvg.thunder.legacy.chat

import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.internal.InlineOnly
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

@InlineOnly
@Suppress("UNUSED")
inline fun onFormatChat(
    regex: Regex,
    consumer: Consumer<MatchResult>,
) = ChatListener.ofFormatted(regex, consumer).apply { register() }

@InlineOnly
@Suppress("UNUSED")
inline fun onUnformatChat(
    regex: Regex,
    consumer: Consumer<MatchResult>,
) = ChatListener.ofUnformatted(regex, consumer).apply { register() }

@JvmField
val mcTextFormatRegex: Pattern = Pattern.compile("(?i)§[0-9A-FK-OR]")

@Suppress("UNUSED")
fun String.removeMcFormat(): String = mcTextFormatRegex.matcher(this).replaceAll("")

@InlineOnly
val String.asChatComponent: IChatComponent
    get() = ChatComponentText(this)