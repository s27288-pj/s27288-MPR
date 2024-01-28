plugins {
	java
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "org.bank"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.projectlombok:lombok:1.18.30")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation(platform("org.junit:junit-bom:5.9.2"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
