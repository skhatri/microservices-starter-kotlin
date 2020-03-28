package com.github.starter.app.config;

import com.github.starter.core.exception.ConfigurationException
import io.r2dbc.spi.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import reactor.core.publisher.Mono
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Scanner
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.Exception
import kotlin.String
import kotlin.Unit
import kotlin.streams.toList

class JdbcScriptProcessor {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JdbcScriptProcessor::class.java);
    }

    fun process(loadResource: String, conn: Connection): Unit {
        val files: List<Resource> = loadFiles(loadResource);
        files.forEach { f -> LOGGER.info("sql file {}", f.filename) };

        val scripts: List<ExecutionScript> = files.map { resourceFile ->
            val statements = tokenise(resourceFile);
            ExecutionScript(resourceFile.filename!!, statements)
        }
        scripts.forEach { executionScript ->
            executionScript.commands
                .forEach { command ->
                    val stmt = conn.createStatement(command);
                    val updated = Mono.from(Mono.from(stmt.execute()).block()!!.rowsUpdated).block();
                    LOGGER.info("running {}, updated {} ", command, updated);
                }
        }
    }

    private fun tokenise(sqlFile: Resource): List<String> {
        try {
            val scanner = Scanner(sqlFile.inputStream, StandardCharsets.UTF_8);
            scanner.useDelimiter(";");
            var items = listOf<String>();
            while (scanner.hasNext()) {
                val segment = scanner.next();
                if (segment.trim().isNotEmpty()) {
                    items = items.plus(segment);
                }
            }
            return items;
        } catch (ex: Exception) {
            throw  ConfigurationException(String.format("error tokenising %s", sqlFile.filename), ex);
        }
    }

    private fun loadFiles(path: String): List<Resource> {
        val prefix = "file:";
        val scriptPattern = Pattern.compile("^V([0-9]+)_(.+)sql$");
        try {
            var resourceList: List<Resource>
            if (path.startsWith(prefix)) {
                val file = File(path.substring(prefix.length));
                resourceList = walkPath(file).stream().map { path1 -> FileSystemResource(path1.toFile()) }.toList();
            } else {
                val pathResourceResolver = PathMatchingResourcePatternResolver();
                val resources = pathResourceResolver.getResources("$path/*.sql");
                resourceList = resources.toList();
            }
            return resourceList.filter { p -> scriptPattern.matcher(p.filename).matches() }
                .sortedWith(Comparator { o1, o2 ->
                    val matchResult1 = scriptPattern.matcher(o1.filename);
                    val matchResult2 = scriptPattern.matcher(o2.filename);
                    if (matchResult1.find() && matchResult2.find()) {
                        val fileNum1 = Integer.parseInt(matchResult1.group(1));
                        val fileNum2 = Integer.parseInt(matchResult2.group(1));
                        fileNum1.compareTo(fileNum2);
                    } else {
                        0
                    }
                })
                .toList();
        } catch (exp: Exception) {
            throw  ConfigurationException("could not load initialise scripts", exp);
        }
    }

    private fun walkPath(file: File): List<Path> = Files.walk(file.toPath()).toList()
}
