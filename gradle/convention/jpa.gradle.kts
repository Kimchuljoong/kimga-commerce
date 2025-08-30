plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
git
val generated = file("src/main/generated")

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(generated)
}

sourceSets {
    main { kotlin.srcDirs += generated }
}

kapt { generateStubs = true }

tasks.named("clean") {
    doLast { generated.deleteRecursively() }
}
