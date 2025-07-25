package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.forge.ForgeEvent
import net.llvg.thunder.legacy.forge.ForgeEventEvent

internal fun postForgeEventEvent(event: ForgeEvent) =
    ForgeEventEvent.of(event).post()