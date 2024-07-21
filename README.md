# User Service

## Description

This microservice is responsible for accepting and saving user migration data.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Running Tests](#running-tests)
- [Dockerization](#dockerization)
- [Contact Information](#contact-information)

## Prerequisites

List the software required to run and develop the microservice:

* Java 17
* Gradle
* Kafka
* PostgreSql
* Docker

## Installation

### Cloning the Repository

```sh
git clone https://github.com/mlipilov/user-service.git
cd user-service
```

### Building the project

```sh
./gradlew build
```

### Running locally

```sh
./gradlew bootRun
```

## Configuration

### Application properties

Application properties are divided into several parts:

* [application.yaml](src%2Fmain%2Fresources%2Fapplication.yaml) - activates all sub-files
* [application-batch.yaml](src%2Fmain%2Fresources%2Fapplication-batch.yaml) - configures Spring
  Batch
* [application-datasource.yaml](src%2Fmain%2Fresources%2Fapplication-datasource.yaml) - configures
  Datasource
* [application-kafka.yaml](src%2Fmain%2Fresources%2Fapplication-kafka.yaml) - configures Kafka
* [application-liquibase.yaml](src%2Fmain%2Fresources%2Fapplication-liquibase.yaml) - configures
  Liquibase
* [application-log.yaml](src%2Fmain%2Fresources%2Fapplication-log.yaml) - configures Logging

### Environment Variables

These can be edited to satisfy your local needs

* DB_LOGIN - Database login
* DB_PASS - Database password
* DB_URL - Database URL
* KAFKA_SERVER_URL - Kafka bootstrap server url

## Usage

### API and request examples

This service do not have any API yet

### Authentication

This service uses only Kafka as the only one communication unit, therefore there is no auth

## Running tests

### Unit & Integration tests

In order to run all the tests execute this command:

```sh
./gradlew test
```

### Test Coverage

To measure the test coverage of your application, you can use Jacoco, which is integrated with
Gradle.
To generate a test coverage report, run the following command:

```sh
./gradlew jacocoTestReport
```

This command will generate a coverage
report: [index.html](build%2Freports%2Fjacoco%2Ftest%2Fhtml%2Findex.html).
You can open the index.html file in this directory to view the detailed coverage report.

## Dockerization

### Building Docker Image

```sh
docker build -t user-service-image .
```

### Running with Docker

```sh
docker run -p 8080:8080 user-service-image
```

**Attention!** When running locally be sure you have provided all the dependencies like DB so the
container can start with no issues.

## Contact Information

If you have any questions, issues, or suggestions regarding this project,
please feel free to reach out to the maintainers listed below.

### Maintainers

Mykhailo Lipilov

* Role: Java Developer
* Email: lipilov.wm@gmail.com