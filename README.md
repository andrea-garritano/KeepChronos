# KeepChronos
[![Build Status](https://travis-ci.org/andryxxx/KeepChronos.svg?branch=master)](https://travis-ci.org/andryxxx/KeepChronos)
[![Coverage Status](https://coveralls.io/repos/github/andryxxx/KeepChronos/badge.svg?branch=master)](https://coveralls.io/github/andryxxx/KeepChronos?branch=master)

Simple Time Tracker developed with awesome advanced programming techniques

### SonarQube
From the directory containing docker-compose.yml run

`docker compose up`

Then, from the directory containing pom.xml run

`mvn clean verify sonar:sonar`

or, replacing IP-ADDRESS with the ip address of the SonarQube container

`mvn clean verify sonar:sonar -Dsonar.host.url=http://IP-ADDRESS:9000`
