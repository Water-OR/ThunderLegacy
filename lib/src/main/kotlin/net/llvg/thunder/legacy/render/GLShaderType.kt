package net.llvg.thunder.legacy.render

import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.*

class GLShaderType(
    @JvmField
    val code: Int,
    @JvmField
    val name: String,
) {
    override fun equals(other: Any?): Boolean =
        other is GLShaderType && other.code == code
    
    override fun hashCode(): Int = code
    
    override fun toString(): String = "ShaderType[$code]{name=$name}"
    
    companion object {
        @JvmField
        @Suppress("UNUSED")
        val VERTEX = GLShaderType(GL_VERTEX_SHADER, "Vertex Shader")
        
        @JvmField
        @Suppress("UNUSED")
        val FRAGMENT = GLShaderType(GL_FRAGMENT_SHADER, "Fragment Shader")
        
        @JvmField
        @Suppress("UNUSED")
        val GEOMETRY = GLShaderType(GL_GEOMETRY_SHADER, "Geometry Shader")
    }
}