plugins {
    id("buildlogic.java-common-conventions")
}

dependencies {

    implementation(project(":repositories"))
    implementation(project(":model"))
    testImplementation("com.h2database:h2:2.2.224")
    implementation("com.mysql:mysql-connector-j:9.3.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.4.11") //Per solventar l'error SLF4J
    implementation("org.slf4j:slf4j-api:2.0.9") //Per solventar l'error SLF4J


    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}