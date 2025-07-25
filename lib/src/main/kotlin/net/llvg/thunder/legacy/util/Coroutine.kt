@file:JvmName("CoroutineUtil")

package net.llvg.thunder.legacy.util

import java.util.concurrent.locks.Lock

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