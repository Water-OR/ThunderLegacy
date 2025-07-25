package net.llvg.thunder.legacy.event

interface Event {
    interface Cancellable : Event {
        val cancelled: Boolean
        
        fun cancel()
        
        abstract class Default : Cancellable {
            override var cancelled: Boolean = false
                protected set
            
            override fun cancel() {
                cancelled = true
            }
        }
    }
}