package net.llvg.thunder.legacy.render

import java.lang.AutoCloseable
import net.llvg.thunder.legacy.internal.Internal
import net.llvg.thunder.legacy.util.EmptyRunnable
import net.llvg.utilities.collection.synchronizedGetOrPut
import org.lwjgl.opengl.GL20.*

@Suppress("UNUSED")
class GLProgram(
    vararg shaders: GLShader,
    beforeLink: Runnable = EmptyRunnable,
) : Bindable, AutoCloseable {
    @JvmField
    val id: Int
    
    @Volatile
    var deleted = false
        private set
    
    init {
        require(shaders.map { it.type.code }.toSet().size == shaders.size) {
            "Duplicate shader type detected. This is not permitted."
        }
        
        val id = glCreateProgram()
        assert(id != 0) { "Failed to create program. This is not permitted." }
        this.id = id
        
        shaders.forEach {
            assert(!it.deleted) { "Using deleted shader. This is not permitted." }
            glAttachShader(id, it.id)
        }
        beforeLink.run()
        
        glLinkProgram(id)
        glGetProgrami(id, GL_LINK_STATUS).let {
            if (it == 0) throw GLProgramLinkError(glGetProgramInfoLog(id))
        }
        
        glValidateProgram(id)
        glGetProgrami(id, GL_VALIDATE_STATUS).let {
            if (it == 0) Internal.logger.warn("[GL Program] Program validation warning: {}", glGetProgramInfoLog(id))
        }
        
        shaders.forEach { glDetachShader(id, it.id) }
    }
    
    private val uniforms: MutableMap<String, Int> = HashMap()
    
    operator fun get(uniformName: String) = uniforms.synchronizedGetOrPut(uniformName) {
        glGetUniformLocation(id, uniformName).also {
            if (it < 0) Internal.logger.warn("[GL Program] Program uniform '{}' not found", uniformName)
        }
    }
    
    override fun bind() {
        glUseProgram(id)
    }
    
    override fun unbind() {
        glUseProgram(0)
    }
    
    
    override fun close() {
        if (deleted) return
        synchronized(this) {
            if (deleted) return
            deleted = true
        }
        unbind()
        glDeleteProgram(id)
    }
}