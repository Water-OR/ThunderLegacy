@file:JvmName("ForgeUtil")

package net.llvg.thunder.legacy.forge

import java.util.function.Consumer
import kotlin.internal.InlineOnly
import kotlin.internal.PureReifiable
import net.llvg.utilities.jClass

internal typealias ForgeEvent = net.minecraftforge.fml.common.eventhandler.Event

@InlineOnly
@Suppress("UNUSED")
inline fun <@PureReifiable reified E : ForgeEvent> onForgeEvent(consumer: Consumer<E>) =
    ForgeEventListenerDefault.of(jClass<E>(), consumer).apply { register() }