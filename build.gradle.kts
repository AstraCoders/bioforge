import org.gradle.kotlin.dsl.withType

plugins {
	java
	idea
	id("net.neoforged.moddev") version "2.0.115"
}

version = project.property("mod_version") as String
group = project.property("mod_group_id") as String

base {
	archivesName = project.property("mod_id") as String
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
	// Specify the version of NeoForge to use.
	version = project.property("neo_version") as String

	parchment {
		mappingsVersion = project.property("parchment_mappings_version") as String
		minecraftVersion = project.property("parchment_minecraft_version") as String
	}

	// This line is optional. Access Transformers are automatically detected
	// accessTransformers.add("src/main/resources/META-INF/accesstransformer.cfg")

	// Default run configurations.
	// These can be tweaked, removed, or duplicated as needed.
	runs {
		create("client") {
			client()
			// Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
			systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
		}

		create("server") {
			server()
			programArgument("--nogui")
			systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
		}

		// This run config launches GameTestServer and runs all registered gametests, then exits.
		// By default, the server will crash when no gametests are provided.
		// The gametest system is also enabled by default for other run configs under the /test command.
		create("gameTestServer") {
			type = "gameTestServer"
			systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
		}

		create("dev") {
			client()
			devLogin = true
		}

		create("data") {
			data()

			// example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
			// gameDirectory = project.file("run-data")

			// Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
			programArguments.addAll(
				"--mod", project.property("mod_id") as String,
				"--all",
				"--output", file("src/generated/resources/").getAbsolutePath(),
				"--existing", file("src/main/resources/").getAbsolutePath()

			)
		}

		// applies to all the run configs above
		configureEach {
			// Recommended logging data for a userdev environment
			// The markers can be added/remove as needed separated by commas.
			// "SCAN": For mods scan.
			// "REGISTRIES": For firing of registry events.
			// "REGISTRYDUMP": For getting the contents of all registries.
			systemProperty("forge.logging.markers", "REGISTRIES")

			// Recommended logging level for the console
			// You can set various levels here.
			// Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
			logLevel = org.slf4j.event.Level.DEBUG
		}
	}

	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// mostly optional in a single mod project
		// but multi mod projects should define one per mod
		create(project.property("mod_id") as String) {
			sourceSet(sourceSets.main.get())
		}
	}
}

repositories {
	mavenLocal()

	exclusiveContent {
		forRepository {
			maven {
				name = "GeckoLib"
				setUrl("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
			}
		}
		filter {
			includeGroup("software.bernie.geckolib")
		}
	}
}


dependencies {
	compileOnly("org.projectlombok:lombok:1.18.42")
	annotationProcessor("org.projectlombok:lombok:1.18.42")

	implementation("software.bernie.geckolib:geckolib-neoforge-${project.property("minecraft_version")}:${project.property("geckolib_version")}")
}

tasks.withType(JavaExec::class) {
	javaLauncher = javaToolchains.launcherFor {
		vendor = JvmVendorSpec.JETBRAINS
		languageVersion = java.toolchain.languageVersion
	}
	jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
	val replaceProperties = mapOf(
		"minecraft_version" to project.property("minecraft_version"),
		"minecraft_version_range" to project.property("minecraft_version_range"),
		"neo_version" to project.property("neo_version"),
		"neo_version_range" to project.property("neo_version_range"),
		"loader_version_range" to project.property("loader_version_range"),
		"mod_id" to project.property("mod_id"),
		"mod_name" to project.property("mod_name"),
		"mod_license" to project.property("mod_license"),
		"mod_version" to project.property("mod_version"),
		"mod_authors" to project.property("mod_authors"),
		"mod_description" to project.property("mod_description")
	)
	inputs.properties(replaceProperties)
	expand(replaceProperties)
	from("src/main/templates")
	into("build/generated/sources/modMetadata")
}

sourceSets.main.configure {
	resources.srcDir("src/generated/resources")
	resources.srcDir(generateModMetadata)
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
