# gradle-git-version

Git 标签驱动的 Android 版本管理工具。

## 特性

- 从 Git 标签自动获取 `versionName`
- 自动计算 `versionCode` (major * 10000 + minor * 100 + patch)
- 支持开发版本标识 (如 `1.0.0-dev.5+abc123`)
- 构建时打印版本信息

## 使用方法

### 1. 复制 buildSrc 目录

将 `buildSrc/` 目录复制到你的 Android 项目根目录：

```bash
cp -r buildSrc /path/to/your/android/project/
```

### 2. 在 app/build.gradle.kts 中使用

```kotlin
// 打印版本信息
GitVersions.printVersionInfo(rootDir)

android {
    defaultConfig {
        versionCode = GitVersions.getVersionCode(rootDir)
        versionName = GitVersions.getVersion(rootDir)

        // 可选：添加 BuildConfig 字段供运行时使用
        buildConfigField("String", "GIT_VERSION", "\"${GitVersions.getVersion(rootDir)}\"")
        buildConfigField("int", "GIT_COMMIT_COUNT", "${GitVersions.getCommitCount(rootDir)}")
    }
}
```

### 3. 创建 Git 标签

```bash
# 创建初始版本标签
git tag -a v1.0.0 -m "Initial release"

# 发布新版本
git tag -a v1.1.0 -m "Feature: xxx"
```

## 版本号规则

| 场景 | Git 状态 | versionName | versionCode |
|-----|---------|-------------|-------------|
| 发布版 | 精确在标签 v1.2.3 | `v1.2.3` | `10203` |
| 开发版 | 标签后 5 次提交 | `v1.2.3-dev.5+abc123` | `10203` |
| 补丁版 | 标签 v1.2.3-patch-1 | `v1.2.3-patch-1` | `10203` |
| 无标签 | 仅有 commit | `abc1234` | `1` |

## API 参考

```kotlin
object GitVersions {
    // 获取版本名称
    fun getVersion(rootDir: File): String

    // 获取版本代码
    fun getVersionCode(rootDir: File): Int

    // 获取 Git 提交总数
    fun getCommitCount(rootDir: File): Int

    // 打印版本信息
    fun printVersionInfo(rootDir: File)
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

MIT License - 详见 [LICENSE](LICENSE) 文件
