plugins {
    id("buildlogic.java-common-conventions")
}


dependencies {
    implementation(project(":model"))
    implementation("com.athaydes.rawhttp:")
}