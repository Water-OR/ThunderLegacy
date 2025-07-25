@file:JvmName("LoggerUtil")
@file:Suppress("UNUSED")

package net.llvg.thunder.legacy.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun log4jLogger(name: String): Logger = LogManager.getLogger(name)

fun log4jLogger(value: Any): Logger = LogManager.getLogger(value)

fun log4jLogger(clazz: Class<*>): Logger = LogManager.getLogger(clazz)