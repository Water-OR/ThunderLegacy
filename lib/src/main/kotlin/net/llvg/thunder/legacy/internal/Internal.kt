package net.llvg.thunder.legacy.internal

import net.llvg.thunder.legacy.event.EventManager
import net.llvg.thunder.legacy.event.onEvent
import net.llvg.thunder.legacy.hypixel.HypixelLocation
import net.llvg.thunder.legacy.hypixel.HypixelModAPIHelper
import net.llvg.thunder.legacy.util.log4jLogger
import net.llvg.thunder.legacy.vanilla.MinecraftInstantiationEvent
import net.llvg.utilities.jClass

internal object Internal {
    @JvmStatic
    fun init() = Unit // For <clinit> invoke
    
    @JvmField
    internal val logger = log4jLogger("Thunder Internal")
    
    init {
        logger.info(
            "[ThunderLegacy] initializing\nclass: {}\nloader class: {}\nstacktrace: ",
            jClass<Internal>().name,
            jClass<Internal>().classLoader.jClass.name,
            Throwable("Print current stacktrace")
        )
        
        EventManager
        HypixelModAPIHelper
        HypixelLocation
        
        onEvent<MinecraftInstantiationEvent> {
            logger.info("[ThunderLegacy] Welcome to minecraft")
        }
    }
}