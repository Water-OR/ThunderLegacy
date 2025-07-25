package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.vanilla.MinecraftInstantiationEvent
import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.util.wait
import net.minecraft.client.Minecraft

internal fun postMinecraftInstantiationEvent(mc: Minecraft) =
    MinecraftInstantiationEvent.of(mc).post().wait()