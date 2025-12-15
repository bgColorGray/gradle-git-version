/**
 * 示例：如何在 Android 项目中使用 GitVersions
 *
 * 将 buildSrc 目录复制到你的项目后，在 app/build.gradle.kts 中添加以下配置：
 */

// 打印版本信息（构建时显示）
GitVersions.printVersionInfo(rootDir)

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34

        // 使用 Git 标签自动管理版本
        versionCode = GitVersions.getVersionCode(rootDir)
        versionName = GitVersions.getVersion(rootDir)

        // 可选：添加 BuildConfig 字段供运行时使用
        buildConfigField("String", "GIT_VERSION", "\"${GitVersions.getVersion(rootDir)}\"")
        buildConfigField("int", "GIT_COMMIT_COUNT", "${GitVersions.getCommitCount(rootDir)}")
    }

    // ... 其他配置
}

/**
 * 运行时获取版本信息：
 *
 * val version = BuildConfig.GIT_VERSION      // "v1.0.0" 或 "v1.0.0-dev.5+abc123"
 * val commitCount = BuildConfig.GIT_COMMIT_COUNT  // 46
 */
