# Zonky challenge

## Description
The aim of this project is to build a Java application, that place a request every 5 minutes to fetch all "new" Zonky loans in publicly available API ([https://api.zonky.cz/loans/marketplace]) and displays the response. 

With such a "loose functional requirements", I have made these architectural decisions:
- application will be build using Java 11, Spring Boot 2.2 and built using Gradle 5
- application will use MongoDB for persisting fetched data from Zonky API
- application will display fetched data on web page
- application will be fully dockerized  
  

### Key features

- Highly configurable, possible to setup the following:
  - refresh interval to fetch 
  - additional filtering query parameters (e.g. rating__eq=AAA)
  - backward search duration (how many hours in the past should be the application looking for the fresh loans)
  
- Data resilient
  - application is using Mongo DB for data persistence
  
- Cloud ready
  - fully dockerized application, requiring no additional comfiguration/tooling (except from docker) to be run

- Intelligent search
  - uses the already fetched data to alter the search parameters  
  
- User friendly 
  - provides web interface for displaying fetch results application configuration 

## High level application overview

*Scheduler* consumes the configuration and calls the *DataRequestor* in defined intervals.
*DataRequestor* checks, what is the LoanService's last fetched loan. If found, it use its "dateRegistered" date, otherwise is uses the current date minus the configurable interval (`defaultSearchRange`). 
*DateProcessor* obtains the fetched data from  *DataRequestor*, converting them and saving to database.

```                 
                                         configuration
                       
                                      +-----------------+             +------------+
               +---------------+      |                 |             |            |
               |               +----->+  DataRequestor  +------------>+  ZonkyApi  |
               |               |      |                 +-----+       |            |
               |   Scheduler   |      +-----------------+     |       +------------+
               |               |      +-----------------+     |
               |               |      |                 |     |
               |               +----->+  DataProcessor  |     |
               +---------------+      |                 |     |
                                      +--------+--------+     |
                                               |              |
                                      +--------+--------+     |
                                      |                 |     |
                                      |   LoanService   +<----+
                                      |                 |
                                      +--------^--------+
                                               |
               +-----------------+    +--------v--------+
        +----->+                 +--->+                 |
web page       |    Rest API     |    |    Database     |
        <------+                 +<---+                 |
               +-----------------+    +-----------------+
```

## Application requirements
For local development:
- Java 11
- Gradle [Get Gradle](https://gradle.org/install)
- Mongo DB Community 4.2.1 (for local development) [Get Mongo](https://www.mongodb.com/download-center/community?jmp=docs)

For running in container
- Docker [Get Docker](https://hub.docker.com/?overlay=onboarding)

## How to run it in Docker
To simplify the runtime, just execute the following command to run the application in the docker container 

```
docker-compose up
```
Application will start and will be serving the web interface under _http://localhost:8080_

_Please note that the docker has to build the images first (hence download all the dependencies), so expect approximately TODO minutes to complete the build. 
The fetched dependencies are stored in the shared docker volume TODO, so any subsequent build will be using that cache and therefore faster._

To tear down the application, execute: `docker-compose down`

Mongo DB is saving data on Docker shared volume. If you want to start with fresh database in Docker, be sure that remove the volume prior starting.
```
docker volume rm zonkychallenge_mongo-data
```

### Build the application

### Parameters
Currently, customizing the application could be done only through setting application properties prior application start, preferably using ```application.properties``.

- MongoDB connection parameters for docker
```
spring.data.mongodb.port=27017
spring.data.mongodb.host=localhost
spring.data.mongodb.database=loans
spring.data.mongodb.username=zonky
spring.data.mongodb.password=zonky
```
- MongoDB connection parameters for Spring 'dev' profile
```
spring.data.mongodb.uri = mongodb://localhost:27017/loans
```
- Additional search parameters for the marketplace filter. Supports the whole range supported by the Zonky's API, e.g.:
```
zonky.loans.urlParams.rating__eq=AAA
zonky.loans.urlParams.insuranceActive__eq=true
...
```
- Backward search duration (in hours): `zonky.loans.defaultSearchRange=10` 
- Zonky's marketplace URL: `zonky.loans.url = https://api.zonky.cz/loans/marketplace`
- Refresh interval for requesting Zonky's marketplace URL: `zonky.loans.refreshInterval = 300000`

Please note, that when changing the application.properties, the docker image has to be rebuilt in order to affect the changes. To do so, please execute:
```
docker-compose up --build
```

### Running in Local development environment
To build the application, execute:
```
./gradlew clean build
```

To run the application in development mode, execute: 
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
- web application does not implement any data conversion, formatting, etc.  
- conversion between Zonky API and domain objects may be done using existing frameworks (possible evenusing Spring's Converter)
- Dockerfile is not written using multi stage build, because in would dramatically increase build time (or would require additional setup to use host's gradle cache)
- Docker-compose and Dockerfile are not using ENTRYPOINT, so passing arguments is not possible
