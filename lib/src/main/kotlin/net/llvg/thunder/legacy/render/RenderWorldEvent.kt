package net.llvg.thunder.legacy.render

import net.llvg.thunder.legacy.event.Event
import net.minecraft.client.renderer.RenderGlobal
import org.joml.Matrix4f

sealed interface RenderWorldEvent : Event {
    val cameraMatrix: Matrix4f
    
    val playerMatrix: Matrix4f
    
    val renderGlobal: RenderGlobal
    
    val partialTicks: Float
    
    companion object {
        private class Impl(
            override val cameraMatrix: Matrix4f,
            override val playerMatrix: Matrix4f,
            override val renderGlobal: RenderGlobal,
            override val partialTicks: Float,
        ) : RenderWorldEvent
        
        internal fun of(
            cameraMatrix: Matrix4f,
            playerMatrix: Matrix4f,
            renderGlobal: RenderGlobal,
            partialTicks: Float,
        ): RenderWorldEvent = Impl(
            cameraMatrix,
            playerMatrix,
            renderGlobal,
            partialTicks,
        )
    }
}