package net.llvg.thunder.legacy.forge

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock
import net.llvg.thunder.legacy.event.EventManager
import net.llvg.utilities.collection.MapWithDefault
import net.llvg.utilities.jClass

object ForgeEventManager {
    init {
        EventManager.register(jClass<ForgeEventEvent>()) { event, collector ->
            if (event !is ForgeEventEvent) return@register
            val forgeEvent = event.event
            val (lock, listeners) = listenersMap.synchronizedGet(forgeEvent.jClass)
            lock.readLock().withLock { for (l in listeners) l[forgeEvent, collector] }
        }
    }
    
    private val listenersMap =
        MapWithDefault<Class<out ForgeEvent>, Pair<ReadWriteLock, MutableList<ForgeEventListener>>> { ReentrantReadWriteLock() to ArrayList() }
    
    @JvmStatic
    fun register(type: Class<out ForgeEvent>, listener: ForgeEventListener) {
        val (lock, listeners) = listenersMap.synchronizedGet(type)
        lock.writeLock().withLock { listeners += listener }
    }
}