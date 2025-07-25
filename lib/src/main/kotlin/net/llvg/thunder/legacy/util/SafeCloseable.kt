package net.llvg.thunder.legacy.util

import java.lang.AutoCloseable

@FunctionalInterface
fun interface SafeCloseable : AutoCloseable {
    override fun close()
}