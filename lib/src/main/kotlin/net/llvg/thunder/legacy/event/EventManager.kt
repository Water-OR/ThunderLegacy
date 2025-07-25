package net.llvg.thunder.legacy.event

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.llvg.utilities.asExtending
import net.llvg.utilities.asList
import net.llvg.utilities.collection.MapWithDefault
import net.llvg.utilities.collection.synchronizedGetOrPut
import net.llvg.utilities.collection.unmodifiable
import net.llvg.utilities.isInType
import net.llvg.utilities.jClass

object EventManager {
    private typealias EventType = Class<out Event>
    
    private val scope = CoroutineScope(SupervisorJob())
    
    private val asyncListenersMap: MapWithDefault<EventType, Pair<ReadWriteLock, MutableList<EventListener.Async>>> =
        MapWithDefault { ReentrantReadWriteLock() to ArrayList() }
    
    private val blockListenersMap: MapWithDefault<EventType, Pair<ReadWriteLock, MutableList<EventListener.Block>>> =
        MapWithDefault { ReentrantReadWriteLock() to ArrayList() }
    
    @JvmStatic
    fun registerAsync(type: EventType, listener: EventListener.Async) {
        asyncListenersMap.synchronizedGet(type).let { (lock, listeners) ->
            lock.writeLock().withLock { listeners += listener }
        }
    }
    
    @JvmStatic
    fun registerBlock(type: EventType, listener: EventListener.Block) {
        blockListenersMap.synchronizedGet(type).let { (lock, listeners) ->
            lock.writeLock().withLock { listeners += listener }
        }
    }
    
    @JvmStatic
    @JvmOverloads
    fun post(type: EventType, event: Event, context: CoroutineContext = EmptyCoroutineContext): Job {
        require(event.isInType(type)) {
            "Event $event is not in type '${type.name}'. This is not permitted."
        }
        
        return runBlocking {
            val jobs = mutableListOf<Job>()
            val blocks = mutableListOf<suspend () -> Unit>()
            EventTypeCache[type].map {
                scope.launch(context) {
                    asyncListenersMap.synchronizedGet(it).let { (lock, listeners) ->
                        lock.readLock().withLock {
                            for (l in listeners) l[event, jobs::add]
                        }
                    }
                    blockListenersMap.synchronizedGet(it).let { (lock, listeners) ->
                        lock.readLock().withLock {
                            for (l in listeners) l[event, blocks::add]
                        }
                    }
                }
            }.joinAll()
            blocks.forEach { it() }
            scope.launch(context) { jobs.joinAll() }
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