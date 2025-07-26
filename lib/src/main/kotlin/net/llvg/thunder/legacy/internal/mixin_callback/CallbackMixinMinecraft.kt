package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.vanilla.GameLoopEvent
import net.llvg.thunder.legacy.vanilla.GameShutdownEvent
import net.llvg.thunder.legacy.vanilla.GameStartedEvent
import net.llvg.thunder.legacy.vanilla.WorldChangeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient

internal fun postGameStartedEvent(mc: Minecraft) =
    GameStartedEvent.of(mc).post()

internal fun postGameShutdownEvent() =
    GameShutdownEvent.of().post()

internal fun postGameLoopEvent() =
    GameLoopEvent.of().post()

internal fun postWorldChangeEvent(
    oldWorld: WorldClient?,
    newWorld: WorldClient?,
) = WorldChangeEvent.of(oldWorld, newWorld).post()