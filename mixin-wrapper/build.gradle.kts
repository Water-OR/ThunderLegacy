import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

val shade by configurations.registering {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val bundle by configurations.registering {
    isCanBeConsumed = true
    isCanBeResolved = false
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public") {
        content { includeGroup("org.spongepowered") }
    }
}

@Suppress("UnstableApiUsage")
dependencies {
    shade("com.google.guava:guava:33.4.8-jre")
    shade(libs.mixin.fabric) {
        exclude(group = "com.google.guava")
        exclude(group = "com.google.code.gson")
    }
    shade(libs.mixin.extras.common)
}

val combineMixinBundle by tasks.registering(ShadowJar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations = listOf(shade.get())
    exclude("META-INF/*.SF", "META-INF/*.RSA")
    exclude("**/module-info.class")
    exclude("META-INF/LICENSE")
    exclude("META-INF/README")
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")
    relocate("com.google", "libs.com.google")
    enabled = true
}

tasks {
    withType<Jar> { enabled = name == combineMixinBundle.name }
    assemble { dependsOn(combineMixinBundle) }
    
    build { enabled = false }
    
    findByName("generateMetadataFileForMixin-wrapperPublication")?.apply {
        dependsOn(combineMixinBundle)
    }
}

artifacts {
    add(bundle.name, combineMixinBundle)
}