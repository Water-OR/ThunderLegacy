import dev.architectury.pack200.java.Pack200Adapter
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.loom)
    alias(libs.plugins.shadow)
    `java-library`
}

val modVersion = (property("version") as String).trim()
val modTweaker = (property("tweaker") as String).trim().takeIf { !it.isEmpty() }

val mixinConfig = "mixin.thunder-legacy.json"
val mixinRefMap = "mixin.thunder-legacy.refmap.json"

val accessTransformer = sourceSets.main.get().resources.srcDirs.firstNotNullOfOrNull {
    it.listFiles().firstOrNull { f -> f.isFile && f.name.endsWith("_at.cfg") }
}

group = "net.llvg"
version = modVersion
base.archivesName = "Thunder Legacy"

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
        mixinConfig(mixinConfig)
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
    isCanBeConsumed = true
}

val shadeRuntimeOnly by configurations.registering {
    isCanBeConsumed = false
}

configurations {
    implementation.extendsFrom(shade)
    runtimeOnly.extendsFrom(shadeRuntimeOnly)
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
}

@Suppress("UnstableApiUsage")
dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
    
    modRuntimeOnly(libs.devAuth)
    
    shade(libs.bundles.kotlin)
    shade(libs.llvgUtils)
    shade(libs.tweakerFix)
    shade(libs.joml)
    
    compileOnly(libs.mixin.fabric) {
        exclude(group = "com.google.guava")
        exclude(group = "com.google.code.gson")
    }
    compileOnly(libs.mixin.extras.common)
    
    shadeRuntimeOnly(project(":mixin-wrapper", "bundle"))
    
    libs.mixin.asProvider().get().run {
        annotationProcessor(group, name, version, classifier = "processor")
    }
    annotationProcessor(libs.mixin.extras.common)
}

tasks {
    processResources {
        inputs.property("mixinConfig", mixinConfig)
        inputs.property("mixinRefMap", mixinRefMap)
        
        filesMatching(mixinConfig) {
            expand("refmap" to mixinRefMap)
        }
    }
    
    shadowJar {
        archiveClassifier = "all"
        configurations = listOf(shade, shadeRuntimeOnly).map { it.get() }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
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