package net.llvg.thunder.legacy.event

class CancelContext(
    private val e: Event.Cancellable
) {
    val cancelled: Boolean
        get() = e.cancelled
    
    fun cancel() = e.cancel()
}