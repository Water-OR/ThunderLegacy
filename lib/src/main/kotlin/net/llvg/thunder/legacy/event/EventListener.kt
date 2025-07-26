package net.llvg.thunder.legacy.event

import java.util.function.Consumer

fun interface EventListener {
    operator fun get(event: Event, collector: Consumer<Runnable>)
}