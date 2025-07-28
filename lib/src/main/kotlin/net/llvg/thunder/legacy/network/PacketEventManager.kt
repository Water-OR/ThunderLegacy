package net.llvg.thunder.legacy.network

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock
import net.llvg.thunder.legacy.event.EventManager
import net.llvg.thunder.legacy.event.asContext
import net.llvg.utilities.asExtending
import net.llvg.utilities.asList
import net.llvg.utilities.collection.MapWithDefault
import net.llvg.utilities.collection.synchronizedGetOrPut
import net.llvg.utilities.collection.unmodifiable
import net.llvg.utilities.jClass
import net.minecraft.network.Packet

object PacketEventManager {
    private val listenersInMap =
        MapWithDefault<Class<out Packet<*>>, Pair<ReadWriteLock, MutableList<PacketEventListener>>> { ReentrantReadWriteLock() to ArrayList() }
    
    private val listenersOutMap =
        MapWithDefault<Class<out Packet<*>>, Pair<ReadWriteLock, MutableList<PacketEventListener>>> { ReentrantReadWriteLock() to ArrayList() }
    
    init {
        EventManager.register(jClass<PacketEvent.Inbound>()) { event, collector ->
            if (event !is PacketEvent) return@register
            val packet = event.packet
            
            event.asContext.run {
                PacketTypeCache[packet.jClass].forEach {
                    val (lock, listeners) = listenersInMap.synchronizedGet(it)
                    lock.readLock().withLock { for (l in listeners) l[packet, collector] }
                }
            }
        }
        EventManager.register(jClass<PacketEvent.Outbound>()) { event, collector ->
            if (event !is PacketEvent) return@register
            val packet = event.packet
            
            event.asContext.run {
                PacketTypeCache[packet.jClass].forEach {
                    val (lock, listeners) = listenersOutMap.synchronizedGet(it)
                    lock.readLock().withLock { for (l in listeners) l[packet, collector] }
                }
            }
        }
    }
    
    @JvmStatic
    fun registerIn(type: Class<out Packet<*>>, listener: PacketEventListener) {
        val (lock, listeners) = listenersInMap.synchronizedGet(type)
        lock.writeLock().withLock { listeners += listener }
    }
    
    @JvmStatic
    fun registerOut(type: Class<out Packet<*>>, listener: PacketEventListener) {
        val (lock, listeners) = listenersOutMap.synchronizedGet(type)
        lock.writeLock().withLock { listeners += listener }
    }
    
    private object PacketTypeCache {
        private val classEventType = jClass<Packet<*>>()
        
        private val supertypesMap: MutableMap<Class<out Packet<*>>, Collection<Class<out Packet<*>>>> = HashMap()
        
        private fun build(type: Class<out Packet<*>>): Collection<Class<out Packet<*>>> = buildSet {
            add(type)
            
            type.superclass?.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
            
            for (i in type.interfaces) i.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
        }.toTypedArray().asList.unmodifiable.let { supertypesMap.putIfAbsent(type, it) ?: it }
        
        operator fun get(type: Class<out Packet<*>>): Collection<Class<out Packet<*>>> =
            supertypesMap.synchronizedGetOrPut(type) { build(type) }
    }
}