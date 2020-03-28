rootProject.name="starter-kotlin"

listOf("app", "load-testing").forEach { folder ->
    include(folder)
    project(":${folder}").projectDir = file(folder)
}
