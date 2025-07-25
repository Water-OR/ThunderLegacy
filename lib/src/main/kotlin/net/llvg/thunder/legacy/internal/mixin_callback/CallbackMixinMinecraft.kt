package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.util.wait
import net.llvg.thunder.legacy.vanilla.GameShutdownEvent
import net.llvg.thunder.legacy.vanilla.GameStartedEvent
import net.minecraft.client.Minecraft

internal fun postGameStartedEvent(mc: Minecraft) =
    GameStartedEvent.of(mc).post().wait()

internal fun postGameShutdownEvent() =
    GameShutdownEvent.of().post().wait()