plugins {
    id("buildlogic.java-application-conventions")
}

dependencies {
    implementation(project(":model"))
    implementation(project(":app"))
    implementation(project(":server"))
    implementation(project(":utilities"))

    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0") // per JSON
    // IMPORTANT: Perquè Jackson entengui el tipus 'LocalDate'
    implementation("com.athaydes.rawhttp:rawhttp-core:2.6.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.4")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.test {
    useJUnitPlatform()
}


