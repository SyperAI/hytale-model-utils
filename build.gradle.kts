plugins {
    id("java")
}

group = "net.starman"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly(files("libs/HytaleServer.jar"))
}

tasks.test {
    useJUnitPlatform()
}


val pluginJar = tasks.named("build")
tasks.register<Copy>("copyPluginToServer") {
    dependsOn(pluginJar)

    from(layout.buildDirectory.dir("libs"))
    include("*.jar")

    into(layout.buildDirectory.dir("Server\\Server\\mods"))
}

tasks.register<Exec>("runServer") {
    dependsOn("copyPluginToServer")

    workingDir = file(layout.buildDirectory.dir("Server\\Server"))
    commandLine("java", "-jar", layout.projectDirectory.dir("libs/HytaleServer.jar"), "--assets", "../Assets.zip")
}