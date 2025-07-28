package net.llvg.thunder.legacy.internal.tweak

import java.io.File
import net.hypixel.modapi.tweaker.HypixelModAPITweaker
import net.llvg.thunder.legacy.util.log4jLogger
import net.llvg.utilities.cast
import net.llvg.utilities.jClass
import net.minecraft.launchwrapper.Launch
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.launch.MixinTweaker
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI

private val logger = log4jLogger("Thunder Preload")

private val selfFile = run<_> search@{
    logger.info("[Thunder Preload] start searching self file")
    
    try {
        File(jClass<Tweak>().protectionDomain.codeSource.location.toURI())
    } catch (e: Exception) {
        logger.warn("[Thunder Preload] Failure occur while searching self file", e)
        return@search null
    }.apply {
        if (!isFile) {
            logger.warn("[Thunder Preload] Finish searching self but NOT a file, ignoring '{}'", absoluteFile)
            return@search null
        }
        logger.info("[Thunder Preload] Finish searching self file '{}'", absoluteFile)
    }
}

internal fun nonMixinTweaker() = jClass<MixinTweaker>().name !in Launch.blackboard["TweakClasses"].cast<MutableList<String>>()

internal fun addOtherTweakers() {
    Launch.blackboard["TweakClasses"].cast<MutableList<String>>().run<_, Unit> {
        add(jClass<MixinTweaker>().name)
        add(jClass<HypixelModAPITweaker>().name)
    }
}

internal fun addSelfMixinContainer() {
    selfFile?.run { MixinBootstrap.getPlatform().addContainer(ContainerHandleURI(toURI())) }
}