@file:JvmName("RenderUtil")

package net.llvg.thunder.legacy.render

import kotlin.internal.InlineOnly
import net.minecraft.client.gui.FontRenderer

@InlineOnly
@Suppress("UNUSED")
inline fun String.compileAsShader(type: GLShaderType) = GLShader(type, this)

@InlineOnly
@JvmOverloads
@Suppress("UNUSED")
inline fun FontRenderer.drawCenteredString(
    text: String,
    x: Float,
    y: Float,
    color: Int,
    dropShadow: Boolean = false
): Int =
    drawString(text, x - getStringWidth(text) / 2f, y, color, dropShadow)