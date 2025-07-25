@file:JvmName("BuilderUtil")

package net.llvg.thunder.legacy.util

import kotlin.internal.InlineOnly

@InlineOnly
operator fun <R, B : BuilderBase<R>> B.invoke(configure: B.() -> Unit): R =
    apply(configure).build()