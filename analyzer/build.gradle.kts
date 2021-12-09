
plugins {
    `java-library`
}

dependencies {
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    testImplementation("junit:junit:4.+")
    implementation("org.json:json:20210307")

}


