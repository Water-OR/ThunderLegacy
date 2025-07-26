package net.llvg.thunder.legacy.forge

import java.util.function.Consumer

fun interface ForgeEventListener {
    operator fun get(event: ForgeEvent, collector: Consumer<Runnable>)
}