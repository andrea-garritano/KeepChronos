# KeepChronos
[![Build Status](https://travis-ci.org/andryxxx/KeepChronos.svg?branch=master)](https://travis-ci.org/andryxxx/KeepChronos)
[![Coverage Status](https://coveralls.io/repos/github/andryxxx/KeepChronos/badge.svg?branch=master)](https://coveralls.io/github/andryxxx/KeepChronos?branch=master)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.garritano%3Akeepchronos&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.garritano%3Akeepchronos)

Simple Time Tracker developed with awesome advanced programming techniques

## Prerequisites

* [Docker](https://www.docker.com/)
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3.3](https://maven.apache.org/download.cgi)

## Running

* `git clone <repository-url>` this repository
* `cd keepchronos`
* `docker compose up`
* `mvn install`

### SonarQube
From the main directory run

* `docker compose up`
* `cd keepchronos`
* `mvn clean verify sonar:sonar`
