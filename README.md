# microservices-starter

Microservices Starter Project

[![Build](https://travis-ci.com/skhatri/microservices-starter-kotlin.svg?branch=master)](https://travis-ci.com/github/skhatri/microservices-starter-kotlin)
[![Code Coverage](https://img.shields.io/codecov/c/github/skhatri/microservices-starter-kotlin/master.svg)](https://codecov.io/github/skhatri/microservices-starter-kotlin?branch=master)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3826/badge)](https://bestpractices.coreinfrastructure.org/projects/3826)
[![Maintainability](https://api.codeclimate.com/v1/badges/a6e61daff59106617104/maintainability)](https://codeclimate.com/github/skhatri/microservices-starter-kotlin/maintainability)
[![Known Vulnerabilities](https://snyk.io/test/github/skhatri/microservices-starter-kotlin/badge.svg?targetFile=build.gradle.kts)](https://snyk.io/test/github/skhatri/microservices-starter-kotlin?targetFile=build.gradle.kts)

### Development

[Developer Guide](DEV.md)

### Content

| Category     | Choice     |
|--------------|------------|
| Logging      | log4j2     |
| SAST         | Sonar      |
| Tests        | JUnit5     |
| Coverage     | Jacoco     |
| Code Style   | Checkstyle |
| Load Testing | Gatling    |

### load-testing
Gatling

Load test can be run using one of the following two approaches

```
gradle load-testing:runTest
IDE - com.github.starter.todo.Runner
```

### vulnerability

Install snyk and authenticate for CLI session

```
npm install -g snyk
snyk auth
```

Publish results using

```
snyk monitor --all-sub-projects
```
