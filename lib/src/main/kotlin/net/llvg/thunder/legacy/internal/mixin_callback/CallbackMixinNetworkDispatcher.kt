package net.llvg.thunder.legacy.internal.mixin_callback

import io.netty.channel.ChannelPipeline
import net.llvg.thunder.legacy.internal.network.ThunderLegacyPacketHandler

internal fun injectHandler(pipeline: ChannelPipeline) {
    pipeline.addAfter("fml:packet_handler", "ThunderLegacyPacketHandler", ThunderLegacyPacketHandler)
}