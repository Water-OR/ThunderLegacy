@file:JvmName("CombinedClosableExtensions")

package net.llvg.thunder.legacy.util

import kotlin.internal.InlineOnly

context(c: CombinedClosable)
@InlineOnly
@Suppress("UNUSED")
inline fun <C : AutoCloseable> C.use(): C = apply(c::include)

context(c: CombinedClosable)
@InlineOnly
inline operator fun <C : AutoCloseable> C.unaryPlus(): C = apply(c::include)

@InlineOnly
inline operator fun <R> CombinedClosable.invoke(action: CombinedClosable.() -> R): R = action()