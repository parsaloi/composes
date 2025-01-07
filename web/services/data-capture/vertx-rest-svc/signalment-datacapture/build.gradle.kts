import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
        application
        id("com.gradleup.shadow") version "8.3.3"
        id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "co.example"
version = "1.0.0-SNAPSHOT"

repositories {
        mavenCentral()
}

val vertxVersion = "4.5.10"
val junitJupiterVersion = "5.11.2"

val mainVerticleName = "co.example.MainApplication"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
        mainClass.set(launcherClassName)
}

dependencies {
        implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
        implementation("io.reactivex.rxjava3:rxjava:3.1.9")
        implementation("io.vertx:vertx-rx-java3")
        implementation("io.vertx:vertx-jdbc-client")
        implementation("io.vertx:vertx-web")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
        implementation("io.vertx:vertx-kafka-client")
        implementation("io.vertx:vertx-pg-client")
        implementation("io.vertx:vertx-sql-client")
        implementation("com.ongres.scram:client:2.1")
        testImplementation("net.jqwik:jqwik:1.9.1")
        testImplementation("io.vertx:vertx-junit5")
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        
	implementation("ch.qos.logback:logback-classic:1.5.12")

        implementation("org.hsqldb:hsqldb:2.7.3")
        implementation("io.codenotary:immudb4j:1.0.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<ShadowJar>().configureEach {
        archiveClassifier.set("fat")
        manifest {
                attributes(mapOf("Main-Verticle" to mainVerticleName))
        }
        mergeServiceFiles()
}

tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
                events("passed", "skipped", "failed")
        }
}

tasks.withType<JavaExec>().configureEach {
        args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
        systemProperties["vertx.logger-delegate-factory-class-name"] = "io.vertx.core.logging.SLF4JLogDelegateFactory"
}
