plugins {
    id("maven-publish")
}

val nexusDeployUser: String = extra.has("nexusDeployUser").let {
    if (it) { extra.get("nexusDeployUser") as String }
    else { System.getenv("nexus_user") ?: "" }
}
val nexusDeployPassword: String = extra.has("nexusDeployPassword").let {
    if (it) { extra.get("nexusDeployPassword") as String }
    else { System.getenv("nexus_password") ?: "" }
}

val artifactFile: String = System.getProperty("artifact")
var id: String = System.getProperty("id")

publishing {
    publications {
        create<MavenPublication>("deploymentBundle") {
            artifact(artifactFile){
                extension = "zip"
                groupId = "com.example.id"
                version = version
            }
            afterEvaluate {
                artifactId = "${id}-deployment-bundle"
            }
        }
    }
    repositories {
        maven {
            name = customer
            credentials {
                username = nexusDeployUser
                password = nexusDeployPassword
            }
            url = uri("https://nexus.example.com/repository/deploys")
        }
    }
}