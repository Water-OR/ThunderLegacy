@file:JvmName("FunctionUtil")

package net.llvg.thunder.legacy.util

object EmptyRunnable : Runnable {
    override fun run() = Unit
    
    @JvmStatic
    fun of(): Runnable = EmptyRunnable
}