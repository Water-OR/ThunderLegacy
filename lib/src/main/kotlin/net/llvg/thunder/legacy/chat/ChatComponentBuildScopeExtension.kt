@file:JvmName("ChatComponentBuildScopeExtension")

package net.llvg.thunder.legacy.chat

import java.util.function.Consumer
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.internal.InlineOnly
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

context(e: ChatComponentBuildScope)
@InlineOnly
inline operator fun Consumer<IChatComponent>.not() = accept(e.last)

context(e: ChatComponentBuildScope)
@InlineOnly
inline operator fun String.unaryPlus() = e.text(ChatComponentText(this))

context(e: ChatComponentBuildScope)
@InlineOnly
inline operator fun IChatComponent.unaryPlus() = e.text(this)

@InlineOnly
@Suppress("UNUSED")
inline fun buildChat(configure: ChatComponentBuildScope.() -> Unit): IChatComponent {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    return buildChat(ChatComponentEmpty(), configure)
}

@InlineOnly
@Suppress("UNUSED")
inline fun buildChat(value: String, configure: ChatComponentBuildScope.() -> Unit): IChatComponent {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    return buildChat(value.asChatComponent, configure)
}

@InlineOnly
inline fun buildChat(value: IChatComponent, configure: ChatComponentBuildScope.() -> Unit): IChatComponent {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    val c = ChatComponentBuildScopeImpl(value)
    try {
        c.configure()
        return value
    } finally {
        c.close()
    }
}