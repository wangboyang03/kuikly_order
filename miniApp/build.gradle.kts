import java.nio.file.Paths

plugins {
  // Import KMM plugin
  kotlin("multiplatform")
}

kotlin {
  // Build JS output for miniApp
  js(IR) {
    // Build output supports browser
    browser {
      webpackTask {
        // Final output executable JS filename
        outputFileName = "miniApp.js"
        webpackConfigApplier {
          val tempConfigFile = File(project.buildDir, "../webpack.config.d/config.js")
          tempConfigFile.parentFile.mkdirs()
          // use node target
          tempConfigFile.writeText(
            """
                        config.target = 'node';
                    """.trimIndent()
          )
          file(tempConfigFile.absolutePath)
        }
      }

      commonWebpackConfig {
        // Do not export global objects, only export necessary entry methods
        output?.library = null
        // use next line to enable source map
        // devtool = org.jetbrains.kotlin.gradle.targets.js.webpack.WebpackDevtool.INLINE_CHEAP_SOURCE_MAP
        devtool = null
      }
    }
    // Package render code and miniApp code together and execute directly
    binaries.executable()
  }
  sourceSets {
    val jsMain by getting {
      dependencies {
        // Import web render
        implementation("com.tencent.kuikly-open.core-render-web:base:${Version.getKuiklyVersion()}")
        implementation("com.tencent.kuikly-open.core-render-web:miniapp:${Version.getKuiklyVersion()}")
      }
    }
  }
}

// Business project path name
val businessPathName = "shared"

/** 先构建业务模块 再从指定目录中筛选出nativevue2.js 复制到统一的业务产物目录 **/
fun registerCopyBusinessBundle(taskName: String, sharedJsTask: String, buildSubPath: String) {
  tasks.register<Copy>(taskName) {
    group = "kuikly"
    dependsOn(":$businessPathName:$sharedJsTask")
    from(Paths.get(project.rootDir.absolutePath, businessPathName, "build/dist/js", buildSubPath).toFile()) {
      include("nativevue2.js")
    }
    into(Paths.get(project.buildDir.absolutePath, "../", "dist", "business").toFile())
  }
}

/** 同步渲染层构建产物 **/
project.afterEvaluate {
  // kotlin 1.9 起产物目录为 $buildDir/dist/js/productionExecutable
  tasks.register<Copy>("syncRenderProductionToDist") {
    from("$buildDir/dist/js/productionExecutable")
    from("$buildDir/kotlin-webpack/js/distributions")
    into("$projectDir/dist/lib")
    include("**/*.js", "**/*.d.ts")
  }

  // kotlin 1.9 起产物目录为 $buildDir/dist/js/developmentExecutable
  tasks.register<Copy>("syncRenderDevelopmentToDist") {
    from("$buildDir/dist/js/developmentExecutable")
    from("$buildDir/kotlin-webpack/js/developmentExecutable")
    into("$projectDir/dist/lib")
    include("**/*.js", "**/*.d.ts")
  }

  /** 复制静态资源 **/
  tasks.register<Copy>("copyAssets") {
    val assetsDir = Paths.get(project.rootDir.absolutePath, businessPathName, "src/commonMain/assets").toFile()
    from(assetsDir)
    into("$projectDir/dist/assets")
    include("**/**")
    exclude("**/.DS_Store")
  }

  tasks.named("jsBrowserProductionWebpack") {
    finalizedBy("syncRenderProductionToDist")
  }

  tasks.named("jsBrowserDevelopmentWebpack") {
    finalizedBy("syncRenderDevelopmentToDist")
  }

  /** 最后阶段 聚合任务 提供MiniApp构建入口 **/
  registerCopyBusinessBundle("copyBusinessProductionToDist", "jsBrowserProductionWebpack", "productionExecutable") // 生产环境任务链
  registerCopyBusinessBundle("copyBusinessDevelopmentToDist", "jsBrowserDevelopmentWebpack", "developmentExecutable") // 开发环境任务链

  tasks.register("jsMiniAppProductionWebpack") {
    group = "kuikly"
    dependsOn("jsBrowserProductionWebpack", "copyBusinessProductionToDist", "copyAssets")
  }

  tasks.register("jsMiniAppDevelopmentWebpack") {
    group = "kuikly"
    dependsOn("jsBrowserDevelopmentWebpack", "copyBusinessDevelopmentToDist", "copyAssets")
  }
}