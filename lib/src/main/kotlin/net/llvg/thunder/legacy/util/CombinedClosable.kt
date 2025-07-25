package net.llvg.thunder.legacy.util

import java.lang.AutoCloseable

class CombinedClosable(
    provider: () -> MutableList<AutoCloseable>
) : AutoCloseable {
    private val collection = provider()
    
    var closed = false
        private set
    
    fun include(resource: AutoCloseable) {
        if (closed) throw UnsupportedOperationException("Can not include resources after closed")
        synchronized(collection) {
            if (closed) throw UnsupportedOperationException("Can not include resources after closed")
            collection += resource
        }
    }
    
    override fun close() {
        if (closed) return
        synchronized(collection) {
            if (closed) return
            closed = true
        }
        collection.asReversed().forEach { it.close() }
    }
}