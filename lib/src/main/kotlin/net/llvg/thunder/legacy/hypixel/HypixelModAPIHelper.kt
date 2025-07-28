package net.llvg.thunder.legacy.hypixel

import kotlin.internal.InlineOnly
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.handler.ClientboundPacketHandler
import net.hypixel.modapi.handler.RegisteredHandler
import net.hypixel.modapi.packet.ClientboundHypixelPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.llvg.utilities.jClass

object HypixelModAPIHelper {
    private val iHypixelModAPI: HypixelModAPI = HypixelModAPI.getInstance()
    
    init {
        iHypixelModAPI.subscribeToEventPacket(jClass<ClientboundLocationPacket>())
    }
    
    @JvmStatic
    @Suppress("UNUSED")
    fun getHypixelModApi() = iHypixelModAPI
    
    @JvmStatic
    fun <T : ClientboundHypixelPacket> listen(type: Class<T>, handler: ClientboundPacketHandler<T>): RegisteredHandler<T> =
        iHypixelModAPI.createHandler(type, handler)
    
    @InlineOnly
    inline fun <reified T : ClientboundHypixelPacket> listen(handler: ClientboundPacketHandler<T>) = listen(jClass<T>(), handler)
}