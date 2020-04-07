# microservices-starter
Microservices Starter Project

[![Build](https://travis-ci.com/skhatri/microservices-starter-kotlin.svg?branch=master)](https://travis-ci.com/github/skhatri/microservices-starter-kotlin)
[![Code Coverage](https://img.shields.io/codecov/c/github/skhatri/microservices-starter-kotlin/master.svg)](https://codecov.io/github/skhatri/microservices-starter-kotlin?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/skhatri/microservices-starter-kotlin/badge.svg?targetFile=build.gradle.kts)](https://snyk.io/test/github/skhatri/microservices-starter-kotlin?targetFile=build.gradle.kts)


### LDAP Features
1. Connect to LDAP
1. Check members of a group
1. Get attribute of a user
1. Create JWT token by signing payload

### Running locally
1. Using docker, `docker-compose up`
1. Run Application.kt
1. `https://localhost/ldap/search/example`

### logging
log4j2

### code analysis
sonar

### testing
junit 5

### code-coverage
Jacoco

### code-style
Google Checkstyle modified to be compatible with 8.30.
Method Length, File Length, Cyclomatic Complexity have been added.

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

### container
build image using
```
gradle jib 
```