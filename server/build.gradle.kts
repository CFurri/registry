plugins {
    id("buildlogic.java-common-conventions")
}


dependencies {
    implementation(project(":model"))
    implementation(project(":jdbc"))

    implementation("com.mysql:mysql-connector-j:9.3.0")
}