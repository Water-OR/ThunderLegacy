@file:JvmName("RenderUtil")

package net.llvg.thunder.legacy.render

import kotlin.internal.InlineOnly

@InlineOnly
@Suppress("UNUSED")
inline fun String.compileAsShader(type: GLShaderType) = GLShader(type, this)
