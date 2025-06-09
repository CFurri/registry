plugins {
    id("buildlogic.java-common-conventions")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("junit:junit:4.13.2")


    implementation(project(":repositories"))
    implementation(project(":model"))

    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("org.hibernate.orm:hibernate-core:7.0.0.Final")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

        testImplementation("com.h2database:h2:2.2.224")

}

tasks.test {
    useJUnitPlatform()
}