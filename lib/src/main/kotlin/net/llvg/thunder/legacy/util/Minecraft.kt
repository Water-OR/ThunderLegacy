@file:JvmName("MinecraftUtil")

package net.llvg.thunder.legacy.util

import net.minecraft.client.Minecraft

val mc: Minecraft
    get() = Minecraft.getMinecraft()