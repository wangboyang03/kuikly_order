import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
  //trick: for the same plugin versions in all sub-modules
  id("com.android.application").version("7.4.2").apply(false)
  id("com.android.library").version("7.4.2").apply(false)
  kotlin("android").version("2.1.21").apply(false)
  kotlin("multiplatform").version("2.1.21").apply(false)
  id("com.google.devtools.ksp").version("2.1.21-2.0.1").apply(false)
  
}

buildscript {
  dependencies {
    classpath(BuildPlugin.kuikly)
  }
}

tasks.withType<KotlinNpmInstallTask>().configureEach {
  args.add("--registry=https://registry.npmmirror.com")
  args.add("--cache=${rootProject.layout.projectDirectory.dir(".npm-cache").asFile.absolutePath}")
}

tasks.register<Copy>("copyOhosAssets") {
  group = "kuikly"
  description = "同步shared的assets资源到鸿蒙工程resfile目录"
  from("shared/src/commonMain/assets")
  into("ohosApp/entry/src/main/resources/resfile")
  include("**/**")
}
