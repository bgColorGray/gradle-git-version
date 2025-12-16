plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.github.bgColorGray"
version = "1.0.0"

repositories {
    mavenCentral()
}

// 将 buildSrc 中的源代码包含进来
sourceSets {
    main {
        kotlin {
            srcDir("buildSrc/src/main/kotlin")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.bgColorGray"
            artifactId = "gradle-git-version"
            version = project.version.toString()

            from(components["java"])
        }
    }
}
