plugins {
    id("java")
}

group = "net.starman"
version = "1.0-SNAPSHOT-pre.1"

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


// Tasks for starting server after build
// If you want to use it you need to change paths to your server installation
// TODO: Automatically download server
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
    commandLine("java", "-jar", layout.projectDirectory.dir("libs/HytaleServer.jar"), "--assets", "../Assets.zip", "--allow-op")
}