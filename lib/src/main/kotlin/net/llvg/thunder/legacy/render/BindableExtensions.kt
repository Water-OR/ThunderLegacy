@file:JvmName("BindableExtensions")

package net.llvg.thunder.legacy.render

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.internal.InlineOnly

@InlineOnly
@Suppress("UNUSED")
inline fun <R> Bindable.hold(action: () -> R): R {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    
    bind()
    try {
        return action()
    } finally {
        unbind()
    }
}