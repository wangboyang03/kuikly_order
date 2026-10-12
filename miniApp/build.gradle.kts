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

  tasks.register<Copy>("copyAssets") {
    val assetsDir = Paths.get(
      project.rootDir.absolutePath,
      businessPathName,
      "src/commonMain/assets"
    ).toFile()
    from(assetsDir)
    into("$projectDir/dist/assets")
    include("**/**")
  }

  tasks.named("jsBrowserProductionWebpack") {
    finalizedBy("syncRenderProductionToDist")
  }

  tasks.named("jsBrowserDevelopmentWebpack") {
    finalizedBy("syncRenderDevelopmentToDist")
  }

  // 业务 JS -> dist/business
  registerCopyBusinessBundle("copyBusinessProductionToDist", "jsBrowserProductionWebpack", "productionExecutable")
  registerCopyBusinessBundle("copyBusinessDevelopmentToDist", "jsBrowserDevelopmentWebpack", "developmentExecutable")

  tasks.register("jsMiniAppProductionWebpack") {
    group = "kuikly"
    dependsOn("jsBrowserProductionWebpack", "copyBusinessProductionToDist")
  }

  tasks.register("jsMiniAppDevelopmentWebpack") {
    group = "kuikly"
    dependsOn("jsBrowserDevelopmentWebpack", "copyBusinessDevelopmentToDist")
  }
}