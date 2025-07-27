@file:JvmName("FunctionUtil")

package net.llvg.thunder.legacy.util

import java.util.function.BooleanSupplier

object EmptyRunnable : Runnable {
    override fun run() = Unit
    
    @JvmStatic
    fun of(): Runnable = EmptyRunnable
}

object TrueSupplier : BooleanSupplier {
    override fun getAsBoolean(): Boolean = true
    
    @JvmStatic
    fun of(): BooleanSupplier = TrueSupplier
}

object FalseSupplier : BooleanSupplier {
    override fun getAsBoolean(): Boolean = false
    
    @JvmStatic
    fun of(): BooleanSupplier = FalseSupplier
}