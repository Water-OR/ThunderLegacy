@file:JvmName("EventUtil")

package net.llvg.thunder.legacy.event

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.internal.InlineOnly
import kotlin.internal.PureReifiable
import net.llvg.utilities.jClass

@InlineOnly
inline fun <@PureReifiable reified E : Event> onEvent(
    block: Boolean = false,
    context: CoroutineContext = EmptyCoroutineContext,
    noinline consumer: suspend (E) -> Unit,
) = EventListenerDefault.of(jClass<E>(), block, context, consumer).apply { register() }

@InlineOnly
inline fun <@PureReifiable reified E : Event> E.post(
    context: CoroutineContext = EmptyCoroutineContext
) = EventManager.post(jClass<E>(), this, context)

@InlineOnly
inline val <E : Event.Cancellable> E.asContext: CancelContext
    get() = CancelContext(this)