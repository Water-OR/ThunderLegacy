@file:JvmName("PacketUtil")

package net.llvg.thunder.legacy.network

import kotlin.internal.InlineOnly
import kotlin.internal.PureReifiable
import net.llvg.thunder.legacy.event.CancelContext
import net.llvg.thunder.legacy.internal.network.ThunderLegacyPacketHandler
import net.llvg.thunder.legacy.util.mc
import net.llvg.utilities.jClass
import net.minecraft.network.Packet

@Suppress("UNUSED")
fun Packet<*>.send() {
    mc.netHandler?.addToSendQueue(this)
}

@Suppress("UNUSED")
fun Packet<*>.sendNoEvent() {
    ThunderLegacyPacketHandler.addNoEventPacket(this)
    mc.netHandler?.addToSendQueue(this)
}

@InlineOnly
@Suppress("UNUSED")
inline fun <@PureReifiable reified P : Packet<*>> onPacketIn(
    noinline consumer: context(CancelContext) (P) -> Unit,
) = PacketEventListenerDefault.ofIn(jClass<P>(), consumer).apply { register() }

@InlineOnly
@Suppress("UNUSED")
inline fun <@PureReifiable reified P : Packet<*>> onPacketOut(
    noinline consumer: context(CancelContext) (P) -> Unit,
) = PacketEventListenerDefault.ofOut(jClass<P>(), consumer).apply { register() }

context(c: CancelContext)
@InlineOnly
@Suppress("UNUSED", "UnusedReceiverParameter")
inline val Packet<*>.cancelled: Boolean
    get() = c.cancelled

context(c: CancelContext)
@InlineOnly
@Suppress("UNUSED", "UnusedReceiverParameter")
inline fun Packet<*>.cancel() = c.cancel()