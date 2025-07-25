@file:JvmName("GLUtil")

package net.llvg.thunder.legacy.render

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.internal.InlineOnly
import org.joml.Matrix2f
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*

@InlineOnly
@Suppress("UNUSED")
fun <R> glHoldAttrib(mask: Int, action: () -> R): R {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    
    glPushAttrib(mask)
    try {
        return action()
    } finally {
        glPopAttrib()
    }
}

fun glGetProgramInfoLog(program: Int): String =
    glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH))

fun glGetShaderInfoLog(shader: Int): String =
    glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH))

@Suppress("UNUSED")
fun glUniform2(location: Int, vector: Vector2f) =
    glUniform2f(location, vector.x, vector.y)

@Suppress("UNUSED")
fun glUniform3(location: Int, vector: Vector3f) =
    glUniform3f(location, vector.x, vector.y, vector.z)

@Suppress("UNUSED")
fun glUniform4(location: Int, vector: Vector4f) =
    glUniform4f(location, vector.x, vector.y, vector.z, vector.w)

private val bufferMatrix2 = createFloatBuffer(4)
@Suppress("UNUSED")
fun glUniformMatrix2(location: Int, transpose: Boolean, matrix: Matrix2f) =
    glUniformMatrix2(location, transpose, matrix[bufferMatrix2])

private val bufferMatrix3 = createFloatBuffer(9)
@Suppress("UNUSED")
fun glUniformMatrix3(location: Int, transpose: Boolean, matrix: Matrix3f) =
    glUniformMatrix3(location, transpose, matrix[bufferMatrix3])

private val bufferMatrix4 = createFloatBuffer(16)
@Suppress("UNUSED")
fun glUniformMatrix4(location: Int, transpose: Boolean, matrix: Matrix4f) =
    glUniformMatrix4(location, transpose, matrix[bufferMatrix4])