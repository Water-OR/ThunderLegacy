@file:JvmName("CoroutineUtil")

package net.llvg.thunder.legacy.util

import java.util.concurrent.locks.Lock
import kotlin.internal.InlineOnly
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 * A method which lock the [lock] and provide an [java.lang.AutoCloseable] object
 * that unlocks the [lock].
 * Use try-with-resources block to unlock automatically.
 *
 * ```java
 * try (SafeCloseable ignored = CoroutineUtil.withLock(lock)) {
 *     // Your code here
 * }
 * ```
 *
 * @param lock The [Lock] to be locked and unlocked
 *
 * @return An [SafeCloseable] instance that unlocks the [lock]
 */
@Suppress("UNUSED")
fun withLock(lock: Lock): SafeCloseable {
    lock.lock()
    return SafeCloseable(lock::unlock)
}

/**
 * A method which blocks the thread until the job completes
 */
@Suppress("UNUSED")
fun wait(job: Job) = runBlocking { job.join() }

/**
 * A method which blocks the thread until the job completes
 */
@InlineOnly
@JvmName("waitReceiver")
inline fun Job.wait() = runBlocking { join() }