import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	war
	kotlin("jvm")
	kotlin("plugin.spring").version("1.5.10")
	kotlin("plugin.serialization")
}

version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

dependencies {
	val versionCoroutine = "1.5.0"
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versionCoroutine}")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${versionCoroutine}")
	implementation(project(":model"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.eclipse.jetty:jetty-reactive-httpclient")
	implementation("org.jsoup:jsoup:1.13.1")
	runtimeOnly("mysql:mysql-connector-java")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "16"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
