package net.llvg.thunder.legacy.hypixel

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlin.jvm.optionals.getOrNull
import net.hypixel.data.type.ServerType
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.llvg.thunder.legacy.event.onEvent
import net.llvg.thunder.legacy.event.post
import net.llvg.thunder.legacy.network.onPacketIn
import net.llvg.thunder.legacy.util.JsonParser
import net.llvg.thunder.legacy.util.contains
import net.llvg.thunder.legacy.vanilla.WorldChangeEvent
import net.minecraft.network.play.server.S02PacketChat

@ConsistentCopyVisibility
data class HypixelLocation
private constructor(
    val serverName: String,
    val serverType: ServerType?,
    val lobbyName: String?,
    val mode: String?,
    val map: String?,
) {
    companion object {
        @JvmStatic
        var instance: HypixelLocation? = null
            private set(value) {
                if (field == value) return
                HypixelLocationChangeEvent.of(field, value).post()
                field = value
            }
        
        init {
            HypixelModAPIHelper.listen<ClientboundLocationPacket> {
                instance = HypixelLocation(
                    it.serverName,
                    it.serverType.getOrNull(),
                    it.lobbyName.getOrNull(),
                    it.mode.getOrNull(),
                    it.map.getOrNull(),
                )
            }
            
            onEvent<WorldChangeEvent> {
                instance = null
            }
            
            onPacketIn<S02PacketChat> {
                val message = it.chatComponent.unformattedText
                if (!message.startsWith('{')) return@onPacketIn
                
                val json = JsonParser.parse(message) as? JsonObject ?: return@onPacketIn
                if ("server" !in json) return@onPacketIn
                val serverName = (json["server"] as? JsonPrimitive ?: return@onPacketIn).asString
                
                instance = HypixelLocation(
                    serverName,
                    (json["gametype"] as? JsonPrimitive)?.run { ServerType.valueOf(this.asString).getOrNull<ServerType>() },
                    (json["lobbyname"] as? JsonPrimitive)?.asString,
                    (json["mode"] as? JsonPrimitive)?.asString,
                    (json["map"] as? JsonPrimitive)?.asString,
                )
            }
        }
    }
}