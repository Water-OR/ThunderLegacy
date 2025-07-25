package net.llvg.thunder.legacy.internal.network

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import java.util.Collections.*
import java.util.WeakHashMap
import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.network.PacketEvent
import net.minecraft.network.Packet

@ChannelHandler.Sharable
internal object ThunderLegacyPacketHandler : ChannelDuplexHandler() {
    internal fun addNoEventPacket(packet: Packet<*>) {
        noEventPackets[packet] = Unit
    }
    
    private val noEventPackets: MutableMap<Packet<*>, Unit> = synchronizedMap(WeakHashMap())
    
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (
            msg !is Packet<*> ||
            !PacketEvent.Inbound.of(msg).apply { post() }.cancelled
        ) super.channelRead(ctx, msg)
    }
    
    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        if (
            msg !is Packet<*> ||
            noEventPackets.remove(msg) !== null ||
            !PacketEvent.Outbound.of(msg).apply { post() }.cancelled
        ) super.write(ctx, msg, promise)
    }
}