package net.llvg.thunder.legacy.event

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock
import net.llvg.utilities.asExtending
import net.llvg.utilities.asList
import net.llvg.utilities.collection.MapWithDefault
import net.llvg.utilities.collection.synchronizedGetOrPut
import net.llvg.utilities.collection.unmodifiable
import net.llvg.utilities.isInType
import net.llvg.utilities.jClass

object EventManager {
    private val listenersMap =
        MapWithDefault<Class<out Event>, Pair<ReadWriteLock, MutableList<EventListener>>> { ReentrantReadWriteLock() to ArrayList() }
    
    @JvmStatic
    fun register(type: Class<out Event>, listener: EventListener) {
        val (lock, listeners) = listenersMap.synchronizedGet(type)
        lock.writeLock().withLock { listeners += listener }
    }
    
    @JvmStatic
    fun post(type: Class<out Event>, event: Event) {
        require(event.isInType(type)) {
            "Event $event is not in type '${type.name}'. This is not permitted."
        }
        
        val actions = mutableListOf<Runnable>()
        EventTypeCache[type].forEach {
            val (lock, listeners) = listenersMap.synchronizedGet(it)
            lock.readLock().withLock { for (l in listeners) l[event, actions::add] }
        }
        actions.forEach { it.run() }
    }
    
    private object EventTypeCache {
        private val classEventType = jClass<Event>()
        
        private val supertypesMap: MutableMap<Class<out Event>, Collection<Class<out Event>>> = HashMap()
        
        private fun build(type: Class<out Event>): Collection<Class<out Event>> = buildSet {
            add(type)
            
            type.superclass?.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
            
            for (i in type.interfaces) i.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
        }.toTypedArray().asList.unmodifiable.let { supertypesMap.putIfAbsent(type, it) ?: it }
        
        operator fun get(type: Class<out Event>): Collection<Class<out Event>> =
            supertypesMap.synchronizedGetOrPut(type) { build(type) }
    }
}