@file:JvmName("MinecraftUtil")

package net.llvg.thunder.legacy.util

import kotlin.internal.InlineOnly
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity

val mc: Minecraft
    get() = Minecraft.getMinecraft()

@Suppress("UNUSED")
@InlineOnly
inline fun Entity.setRotation(
    yaw: Float,
    pitch: Float
) {
    rotationYaw = yaw
    rotationPitch = pitch
}