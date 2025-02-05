buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.puppycrawl.tools:checkstyle:10.21.2")
    }
}
apply(plugin = "checkstyle")


tasks.withType<Checkstyle>().configureEach {
    ignoreFailures = false
    maxErrors = 0
    maxWarnings = 0

    configFile = file("$rootDir/gradle/settings/checkstyle.xml")
    reports {
        xml.required = false
        sarif.required = true
        html.required = true
        html.stylesheet = resources.text.fromFile("$rootDir/gradle/settings/checkstyle.xsl")
    }
}

