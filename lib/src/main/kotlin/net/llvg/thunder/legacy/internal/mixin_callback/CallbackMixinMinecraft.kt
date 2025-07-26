package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.vanilla.GameLoopEvent
import net.llvg.thunder.legacy.vanilla.GameShutdownEvent
import net.llvg.thunder.legacy.vanilla.GameStartedEvent
import net.minecraft.client.Minecraft

internal fun postGameStartedEvent(mc: Minecraft) =
    GameStartedEvent.of(mc).post()

internal fun postGameShutdownEvent() =
    GameShutdownEvent.of().post()

internal fun postGameLoopEvent() =
    GameLoopEvent.of().post()