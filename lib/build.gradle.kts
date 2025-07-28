import dev.architectury.pack200.java.Pack200Adapter
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.loom)
    alias(libs.plugins.shadow)
    `maven-publish`
    `java-library`
}

val modArchive = (property("archive") as String).trim().replace(' ', '_')
val modVersion = (property("version") as String).trim()
val modTweaker = (property("tweaker") as String).trim().takeIf { !it.isEmpty() }

val mixinConfig = (property("mixin_config") as String).trim().run { if (isEmpty()) emptyList() else split('|').map { it.trim() } }
val mixinRefMap = (property("mixin_refmap") as String).trim().takeIf { !mixinConfig.isEmpty() }
val mixinVersion = libs.versions.mixin.asProvider().get()

val accessTransformer = sourceSets.main.get().resources.srcDirs.firstNotNullOfOrNull {
    it.listFiles().firstOrNull { f -> f.isFile && f.name.endsWith("_at.cfg") }
}

group = "net.llvg"
version = modVersion
base.archivesName = modArchive

loom {
    runConfigs {
        "client" {
            property("mixin.debug.export", "true")
            property("mixin.debug.verbose", "true")
            property("mixin.env.disableRefMap", "true")
            modTweaker?.let { programArgs("--tweakClass", it) }
            isIdeConfigGenerated = true
        }
    }
    
    forge {
        pack200Provider = Pack200Adapter()
        mixinConfig(*mixinConfig.toTypedArray())
        accessTransformer?.let(::accessTransformer)
    }
    
    @Suppress("UnstableApiUsage")
    mixin.defaultRefmapName = mixinRefMap
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
    
    withSourcesJar()
    withJavadocJar()
}

kotlin.compilerOptions {
    languageVersion = KotlinVersion.KOTLIN_2_2
    apiVersion = KotlinVersion.KOTLIN_2_2
    
    jvmTarget = JvmTarget.JVM_1_8
    
    freeCompilerArgs.addAll(
        "-Xjvm-expose-boxed",
        "-Xcontext-parameters",
        "-Xnested-type-aliases",
        "-Xcontext-sensitive-resolution",
        "-Xwarning-level=NOTHING_TO_INLINE:disabled",
    )
    
    optIn.addAll(
        "kotlin.contracts.ExperimentalContracts",
        "net.llvg.utilities.KotlinInternal",
    )
    
    verbose = true
}

val shade by configurations.registering {
    isCanBeConsumed = false
}

val shadeRuntimeOnly by configurations.registering {
    isCanBeConsumed = false
}

configurations {
    runtimeClasspath.extendsFrom(shade)
    compileClasspath.extendsFrom(shade)
    runtimeClasspath.extendsFrom(shadeRuntimeOnly)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io") {
        content { includeGroupByRegex("com\\.github\\.(.)+") }
    }
    maven("https://repo.spongepowered.org/repository/maven-public") {
        content { includeGroup("org.spongepowered") }
    }
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") {
        content { includeGroup("me.djtheredstoner") }
    }
    maven("https://repo.hypixel.net/repository/Hypixel") {
        content { includeGroup("net.hypixel") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
    
    modRuntimeOnly(libs.dev.auth)
    
    shade(libs.bundles.kotlin)
    shade(libs.llvg.utils)
    shade(libs.tweaker.fix)
    shade(libs.joml)
    
    compileOnly(libs.hypixel.mod.api.forge.core)
    shade(libs.hypixel.mod.api.forge.tweaker)
    
    compileOnly(libs.mixin.fabric) {
        exclude(group = "com.google.guava")
        exclude(group = "com.google.code.gson")
    }
    compileOnly(libs.mixin.extras)
    
    shadeRuntimeOnly(libs.mixin.wrapper)
    
    annotationProcessor("org.spongepowered:mixin:$mixinVersion:processor")
    annotationProcessor(libs.mixin.extras)
}

tasks {
    processResources {
        inputs.property("mixinConfig", mixinConfig)
        inputs.property("mixinRefMap", mixinRefMap)
        
        filesMatching(mixinConfig) {
            expand("refmap" to mixinRefMap)
        }
        rename("(.+_at.cfg)", "META-INF/$1")
    }
    
    shadowJar {
        archiveClassifier = "all"
        configurations = listOf(shade, shadeRuntimeOnly).map { it.get() }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        relocate("net.hypixel.modapi.tweaker", "net.llvg.thunder.legacy.internal.tweak")
        exclude("META-INF/versions/**")
        mergeServiceFiles()
    }
    
    remapJar {
        inputFile = shadowJar.get().archiveFile
        dependsOn(shadowJar)
    }
    
    jar {
        archiveClassifier = "raw"
        buildMap {
            put("ForceLoadAsMod", true)
            put("ModSide", "CLIENT")
            modTweaker?.let {
                put("TweakClass", it)
                put("TweakOrder", 0)
            }
            accessTransformer?.run { put("FMLAT", name) }
        }.let(manifest::attributes)
        finalizedBy(shadowJar)
    }
}

publishing {
    repositories { mavenLocal() }
    publications {
        register<MavenPublication>("maven") {
            artifactId = modArchive
            artifact(tasks["jar"])
            artifact(tasks["shadowJar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            from(components["kotlin"])
        }
    }
}