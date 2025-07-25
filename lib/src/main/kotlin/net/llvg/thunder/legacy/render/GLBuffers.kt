package net.llvg.thunder.legacy.render

import java.lang.AutoCloseable
import org.lwjgl.opengl.GL15.*

@Suppress("UNUSED")
class GLBuffers(
    @JvmField
    val type: Int
) : Bindable, AutoCloseable {
    
    @JvmField
    val id: Int
    
    @Volatile
    var deleted = false
        private set
    
    init {
        val id = glGenBuffers()
        assert(id != 0) { "Failed to create buffers." }
        
        this.id = id
    }
    
    override fun bind() {
        glBindBuffer(type, id)
    }
    
    override fun unbind() {
        glBindBuffer(type, 0)
    }
    
    override fun close() {
        if (deleted) return
        synchronized(this) {
            if (deleted) return
            deleted = true
        }
        unbind()
        glDeleteBuffers(id)
    }
}