@file:JvmName("EventUtil")

package net.llvg.thunder.legacy.event

import java.util.function.Consumer
import kotlin.internal.InlineOnly
import kotlin.internal.PureReifiable
import net.llvg.utilities.jClass

@InlineOnly
inline fun <@PureReifiable reified E : Event> onEvent(consumer: Consumer<E>) =
    EventListenerDefault.of(jClass<E>(), consumer).apply { register() }

@InlineOnly
inline fun <@PureReifiable reified E : Event> E.post() =
    EventManager.post(jClass<E>(), this)

@InlineOnly
inline val <E : Event.Cancellable> E.asContext: CancelContext
    get() = CancelContext(this)