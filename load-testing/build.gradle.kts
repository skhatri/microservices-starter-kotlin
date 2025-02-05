plugins {
    id("scala")
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.16")
    implementation("org.scala-lang:scala-compiler:2.13.16")
    implementation("io.gatling:gatling-core:3.13.3")
    implementation("io.gatling:gatling-http:3.13.3")
    implementation("io.gatling:gatling-http-client:3.13.3")
    implementation("io.gatling:gatling-charts:3.13.3")
    implementation("io.gatling:gatling-app:3.13.3")
    implementation("io.gatling.highcharts:gatling-charts-highcharts:3.13.3")
}

task("runTest", JavaExec::class) {
    mainClass = "io.gatling.app.Gatling"
    classpath = sourceSets["test"].runtimeClasspath
    args = listOf(
        "-bf", "${sourceSets["test"].output.dirs}",
        "-rsf", "${sourceSets["test"].resources}",
        "-rf", "$projectDir/build/reports/gatling",
        "-s", "com.github.starter.todo.${project.ext["simulation"]}"
    )
    jvmArgs = listOf(
        "-Xms512m", "-Xmx1024m", "-XX:+UseZGC",
        "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED",
        "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-exports=java.base/sun.util.calendar=ALL-UNNAMED"
    )
}

scala {
}
tasks.withType<ScalaCompile>().configureEach {
    scalaCompileOptions.forkOptions.apply {
        memoryMaximumSize = "1g"
        jvmArgs = listOf("-XX:MaxMetaspaceSize=512m")

    }
    scalaCompileOptions.additionalParameters = listOf("-language:postfixOps")

}
