package net.llvg.thunder.legacy.render

import java.lang.AutoCloseable
import org.lwjgl.opengl.GL20.*

class GLShader(
    @JvmField
    val type: GLShaderType,
    @JvmField
    val code: String,
) : AutoCloseable {
    @JvmField
    val id: Int
    
    @Volatile
    var deleted = false
        private set
    
    init {
        val id = glCreateShader(type.code)
        assert(id != 0) { "Failed to create shader. This is not permitted" }
        this.id = id
        
        glShaderSource(id, code)
        glCompileShader(id)
        
        glGetShaderi(id, GL_COMPILE_STATUS).let {
            if (it == 0) throw GLShaderCompileError(type, code, glGetShaderInfoLog(id))
        }
    }
    
    override fun close() {
        if (deleted) return
        synchronized(this) {
            if (deleted) return
            deleted = true
        }
        glDeleteShader(id)
    }
}