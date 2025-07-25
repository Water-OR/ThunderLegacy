package net.llvg.thunder.legacy.internal.mixin_callback

import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.render.RenderWorldEvent
import net.minecraft.client.renderer.RenderGlobal
import org.joml.Matrix4f
import org.lwjgl.BufferUtils.*
import org.lwjgl.opengl.GL11.*

private val tmpBuffer = createFloatBuffer(16)
private val projectionMatrix = Matrix4f()
private val modelViewMatrix = Matrix4f()
private val playerMatrix = Matrix4f()

internal fun postRenderWorldEvent(
    renderGlobal: RenderGlobal,
    partialTicks: Float,
) {
    glMatrixMode(GL_PROJECTION)
    glGetFloat(GL_PROJECTION_MATRIX, tmpBuffer)
    projectionMatrix.set(tmpBuffer)
    glMatrixMode(GL_MODELVIEW)
    glGetFloat(GL_MODELVIEW_MATRIX, tmpBuffer)
    modelViewMatrix.set(tmpBuffer)
    projectionMatrix.mul0(modelViewMatrix, playerMatrix)
    
    RenderWorldEvent.of(
        projectionMatrix,
        playerMatrix,
        renderGlobal,
        partialTicks
    ).post()
}