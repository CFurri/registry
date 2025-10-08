plugins {
    id("buildlogic.java-common-conventions")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))
    implementation(project(":app"))

    implementation("com.mysql:mysql-connector-j:9.3.0")

    implementation(group = "com.athaydes.rawhttp", name = "rawhttp-core", version = "2.6.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0") // per JSON

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
