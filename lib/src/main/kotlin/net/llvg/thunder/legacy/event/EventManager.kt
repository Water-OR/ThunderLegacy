package net.llvg.thunder.legacy.event

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Consumer
import kotlin.concurrent.withLock
import net.llvg.utilities.asExtending
import net.llvg.utilities.asList
import net.llvg.utilities.collection.MapWithDefault
import net.llvg.utilities.collection.synchronizedGetOrPut
import net.llvg.utilities.collection.unmodifiable
import net.llvg.utilities.isInType
import net.llvg.utilities.jClass

object EventManager {
    private typealias EventType = Class<out Event>
    
    private val listenersMap: MapWithDefault<EventType, Pair<ReadWriteLock, MutableList<Consumer<in Event>>>> =
        MapWithDefault { ReentrantReadWriteLock() to ArrayList() }
    
    @JvmStatic
    fun register(type: EventType, listener: Consumer<in Event>) {
        listenersMap.synchronizedGet(type).let { (lock, listeners) ->
            lock.writeLock().withLock { listeners += listener }
        }
    }
    
    @JvmStatic
    fun post(type: EventType, event: Event) {
        require(event.isInType(type)) {
            "Event $event is not in type '${type.name}'. This is not permitted."
        }
        
        EventTypeCache[type].forEach {
            val (lock, listeners) = listenersMap.synchronizedGet(it)
            lock.readLock().withLock { for (l in listeners) l.accept(event) }
        }
    }
    
    private object EventTypeCache {
        private val classEventType = jClass<Event>()
        
        private val supertypesMap: MutableMap<EventType, Collection<EventType>> = HashMap()
        
        private fun build(type: EventType): Collection<EventType> = buildSet {
            add(type)
            
            type.superclass?.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
            
            for (i in type.interfaces) i.asExtending(classEventType)
              ?.let { addAll(supertypesMap[it] ?: build(it)) }
        }.toTypedArray().asList.unmodifiable.let { supertypesMap.putIfAbsent(type, it) ?: it }
        
        operator fun get(type: EventType): Collection<EventType> =
            supertypesMap.synchronizedGetOrPut(type) { build(type) }
    }
}