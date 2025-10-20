plugins {
    id("buildlogic.java-common-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))
    implementation(project(":app"))
    implementation(project(":repositories"))
    implementation(project(":jpa"))
    implementation(project(":jdbc"))

    implementation("com.mysql:mysql-connector-j:9.3.0")

    implementation("com.athaydes.rawhttp:rawhttp-core:2.6.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:5.12.0")

    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}

tasks.test {
    useJUnitPlatform()
}
