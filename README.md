# Zonky challenge

## Description

### Key features
- highly confiugurable
  - bac 
- data resiliency
  - application is using Mongo DB for data persising, 
- intelligent fetch
  - on each run, the DataRequestor
- web interface for dispalying results and aggregations
- cloud ready

## High level application overview

Scheduler
DataRequestor
DataProcessor

                 configuration
                       +
                       |              +-----------------+             +------------+
               +-------v-------+      |                 +------------>+            |
               |               +----->+  DataRequestor  |             |  ZonkyApi  |
               |               |      |                 +<------------+            |
               |   Scheduler   |      +-----------------+             +------------+
               |               |      +-----------------+
               |               |      |                 |
               |               +----->+  DataProcessor  |
               +---------------+      |                 |
                                      +--------+--------+
                                               |
                                      +--------+--------+
                                      |                 |
                                      |   LoanService   |
                                      |                 |
                                      +--------^--------+
                                               |
               +-----------------+    +--------v--------+
        +----->+                 +--->+                 |
web page       |    Rest API     |    |    Database     |
        <------+                 +<---+                 |
               +-----------------+    +-----------------+


## Requirements
The following requirements have to be met:
- Java 11
- Mongo DB Community 4.2.1 (for local development) [Get Mongo](https://www.mongodb.com/download-center/community?jmp=docs)
- Docker (for running in container or for local development) [Get Docker](https://hub.docker.com/?overlay=onboarding)

## How to run it
Just build the code using provided Gradlew wrapper and run the docker compose
```
./gradlew build
docker-compose up --build
```

For detailed instructions, follow the guide below.

### Build the application
To build the application, run:
```
./gradlew clean build
```

Application will start and will be serving the web interface under _http://localhost:8080_

### Parameters
You may also specify
dbHost - database port. Defautled to '27017'.
dbName - database name. Defaulted to `loans`


### Running in production (Docker)
To build the images and start up the containers
```
docker-compose up --build
```

Application will start and will be serving the web interface under _http://localhost:8080_

To tear down the application, execute: `docker-compose down`

Mongo DB is saving data on Docker shared volume. If you want to start with fresh database in Docker, be sure that remove the volume prior starting.
```
docker volume rm zonkychallenge_mongo-data
```

### Running in Local development environment
To run application in development mode, run: 
```
./gradlew bootRun -Dspring.profiles.active=dev
```

Do not forget to change the default spring.data.mongodb.uri parameter in application-dev.properties to match your MongoDB instance connection URI, e.g.:
'``
spring.data.mongodb.uri = mongodb://localhost:27017/whatever
'``
or pass the parameters directly when executing, overriding the properties files, e.g.:
```
./gradlew bootRun -Dspring.profiles.active=dev -Pargs='--spring.data.mongodb.uri=mongodb://localhost:27017/whatever'
```

## Caveats & possible improvements

While working on the solution, I have identified a lot of possible improvements and quick-wins, but chose not to do them mainly to keep the focus on the important stuff. The list of most important/obivous improvements follows:

- database transactions (using @Transactional) as not implemented > because it requires that Mongo DB uses replica sets
- web application is very simple, not using any CSS nor any JS framework > aimed to backend development
- web application not API endpoints are not secured
- Dockerfile is not written using multi stage build, because in would dramatically increase build time (or would require additional setup to use host's gradle cache)
- conversion between Zonky API and domain objects may be done using existing frameworks (possible evenusing Spring's Converter)
