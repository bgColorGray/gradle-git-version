# gradle-git-version

[![](https://jitpack.io/v/bgColorGray/gradle-git-version.svg)](https://jitpack.io/#bgColorGray/gradle-git-version)

Git 标签驱动的 Android/Gradle 版本管理工具。

## 特性

- 从 Git 标签自动获取 `versionName`
- 自动计算 `versionCode` (major × 10000 + minor × 100 + patch)
- 支持开发版本标识 (如 `1.0.0-dev.5+abc123`)
- 构建时打印版本信息

## 安装

### Step 1. 添加 JitPack 仓库

**settings.gradle.kts**
```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2. 添加依赖

**根目录 build.gradle.kts**
```kotlin
buildscript {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath("com.github.bgColorGray:gradle-git-version:1.0.0")
    }
}
```

### Step 3. 在 app 模块中使用

**app/build.gradle.kts**
```kotlin
// 打印版本信息
GitVersions.printVersionInfo(rootDir)

android {
    defaultConfig {
        versionCode = GitVersions.getVersionCode(rootDir)
        versionName = GitVersions.getVersion(rootDir)

        // 可选：添加 BuildConfig 字段
        buildConfigField("String", "GIT_VERSION", "\"${GitVersions.getVersion(rootDir)}\"")
    }
}
```

### Step 4. 创建 Git 标签

```bash
git tag -a v1.0.0 -m "Initial release"
```

## 版本号规则

| 场景 | Git 状态 | versionName | versionCode |
|-----|---------|-------------|-------------|
| 发布版 | 精确在标签 v1.2.3 | `v1.2.3` | `10203` |
| 开发版 | 标签后 5 次提交 | `v1.2.3-dev.5+abc123` | `10203` |

## API

```kotlin
object GitVersions {
    fun getVersion(rootDir: File): String      // 获取版本名
    fun getVersionCode(rootDir: File): Int     // 获取版本代码
    fun getCommitCount(rootDir: File): Int     // 获取提交总数
    fun printVersionInfo(rootDir: File)        // 打印版本信息
}
```

## 构建输出示例

```
═══════════════════════════════════════
  构建版本信息
   版本名称: v1.0.0
   版本代码: 10000
   提交总数: 46
═══════════════════════════════════════
```

## License

MIT
