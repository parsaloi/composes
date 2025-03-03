plugins {
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("java-lts-convention") // Apply the LTS convention from buildSrc
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("net.jqwik:jqwik:1.9.2")
}

// Optional: Customize JavaExec for running the app
tasks.named<JavaExec>("bootRun") {
	mainClass.set("com.example.petclinic.PetclinicApplication") // Replace with your main class
	classpath = sourceSets.main.get().runtimeClasspath
}

// Ensure Jqwik and JUnit 5 work together
tasks.test {
	useJUnitPlatform {
		includeEngines("junit-jupiter", "jqwik")
	}
}
