plugins {
    id("buildlogic.java-common-conventions")
}

dependencies {

    implementation(project(":server"))
    implementation(project(":client"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}