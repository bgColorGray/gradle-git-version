import java.io.File

/**
 * Git 标签驱动的版本管理工具
 *
 * 使用方式：
 *   versionName = GitVersions.getVersion(rootDir)
 *   versionCode = GitVersions.getVersionCode(rootDir)
 *
 * 版本号规则：
 *   - 发布版: "1.0.0" (精确在标签上)
 *   - 开发版: "1.0.0-dev.5+abc123" (标签后有提交)
 *
 * versionCode 算法: major * 10000 + minor * 100 + patch
 *   例如: 1.2.3 → 10203
 */
object GitVersions {

    /**
     * 从 Git 标签获取版本名
     *
     * @param rootDir 项目根目录
     * @return 版本名称字符串
     */
    fun getVersion(rootDir: File): String {
        return try {
            val process = ProcessBuilder("git", "describe", "--tags", "--always", "--long")
                .directory(rootDir)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()

            if (process.exitValue() != 0 || output.isEmpty()) {
                return "1.0.0-dev"
            }

            // 解析 git describe 输出: <tag>-<commits>-g<hash>
            val regex = Regex("""^(.+)-(\d+)-g([a-f0-9]+)$""")
            val match = regex.find(output)

            if (match != null) {
                val (tag, commits, hash) = match.destructured
                if (commits == "0") {
                    tag // 精确在标签上
                } else {
                    "$tag-dev.$commits+$hash" // 开发版本
                }
            } else {
                output // 无标签时返回 commit hash
            }
        } catch (e: Exception) {
            "1.0.0-dev"
        }
    }

    /**
     * 计算版本代码
     *
     * @param rootDir 项目根目录
     * @return 版本代码整数
     */
    fun getVersionCode(rootDir: File): Int {
        return try {
            val version = getVersion(rootDir).split("-")[0]
                .removePrefix("v")
            val parts = version.split(".")
            val major = parts.getOrNull(0)?.toIntOrNull() ?: 1
            val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
            val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
            major * 10000 + minor * 100 + patch
        } catch (e: Exception) {
            1
        }
    }

    /**
     * 获取 Git 提交总数
     *
     * @param rootDir 项目根目录
     * @return 提交总数
     */
    fun getCommitCount(rootDir: File): Int {
        return try {
            val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
                .directory(rootDir)
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            output.toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    /**
     * 打印版本信息（构建时调用）
     *
     * @param rootDir 项目根目录
     */
    fun printVersionInfo(rootDir: File) {
        println("""
        |═══════════════════════════════════════
        |  构建版本信息
        |   版本名称: ${getVersion(rootDir)}
        |   版本代码: ${getVersionCode(rootDir)}
        |   提交总数: ${getCommitCount(rootDir)}
        |═══════════════════════════════════════
        """.trimMargin())
    }
}
