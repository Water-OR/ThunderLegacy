package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.chat.ChatEvent
import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.util.wait
import net.minecraft.util.IChatComponent

internal fun postMessageEvent(text: IChatComponent) =
    ChatEvent.of(text).post().wait()