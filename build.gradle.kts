plugins {
	id("net.fabricmc.fabric-loom-remap")
	kotlin("jvm") version "2.1.0"
	`maven-publish`
	id("com.modrinth.minotaur") version "2.8.7"
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
	maven("https://maven.terraformersmc.com/releases/")
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("pvp-mod") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	// Kotlin (встроен в jar)
	modImplementation("net.fabricmc:fabric-language-kotlin:${providers.gradleProperty("fabric_language_kotlin_version").get()}")
	include("net.fabricmc:fabric-language-kotlin:${providers.gradleProperty("fabric_language_kotlin_version").get()}")

	// ModMenu (опционально)
	modCompileOnly("com.terraformersmc:modmenu:17.0.0")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN"))
	projectId.set("pvp-utility-mod")
	versionNumber.set("${version}")
	versionName.set("PvP Mod ${version}")
	uploadFile.set(tasks.remapJar.get())
	gameVersions.add("1.21.11")
	loaders.add("fabric")
	dependencies {
		required.project("fabric-api")
		required.project("fabric-language-kotlin")
		optional.project("modmenu")
	}
	syncBodyFrom.set(rootProject.file("README.md").readText())
	changelog.set("See https://github.com/FUXKVOB/pvp-mod/releases/tag/v${version}")
}

tasks.modrinth.get().dependsOn(tasks.build)

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
