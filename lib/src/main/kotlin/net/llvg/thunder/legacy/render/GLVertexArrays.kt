package net.llvg.thunder.legacy.render

import java.lang.AutoCloseable
import org.lwjgl.opengl.GL30.*

@Suppress("UNUSED")
class GLVertexArrays : Bindable, AutoCloseable {
    @JvmField
    val id: Int
    
    @Volatile
    var deleted = false
        private set
    
    init {
        val id = glGenVertexArrays()
        assert(id != 0) { "Failed to create vertex arrays." }
        
        this.id = id
    }
    
    override fun bind() {
        glBindVertexArray(id)
    }
    
    override fun unbind() {
        glBindVertexArray(0)
    }
    
    override fun close() {
        if (deleted) return
        synchronized(this) {
            if (deleted) return
            deleted = true
        }
        unbind()
        glDeleteVertexArrays(id)
    }
}