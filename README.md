h1.Zonky challenge

h2. Description

h2. Features
- highly confiugurable
- persisted results 
- web interface for dispalying results and aggregations
- cloud ready


h2. Build & execution
h3. Requirements
The following requirements have to be met to develop:
- Java 11
- Gradle 5
- Mongo DB (for local development)
- Docker (for running in container or for local development)

h3. Build and application parameters
To build the application, run

./gradlew clean build

Application will start and will be serving the web interface under http://localhost:8080

Parameters TODO..
You may also specify
dbHost - database port. Defautled to '27017'.
dbName - database name. Defaulted to 'zonkyLoans'


h3. Running in Docker
To build the images and start up the containers

docker-compose up --build

Application will start and will be serving the web interface under http://localhost:8080

To tear down the application
docker-compose down

Mongo DB is saving data on Docker shared volume. If you want to start with fresh database in Docker, be sure that remove the volume prior starting 

docker volume rm zonkychallenge_mongo-data


h3. Running in Local development environment
To run application in development mode, run 
./gradlew bootRun -Dspring.profiles.active=dev

Do not forget to change the default spring.data.mongodb.uri parameter in application-dev.properties to match your MongoDB instance connection URI, e.g.:

spring.data.mongodb.uri = mongodb://localhost:27017/whatever

or pass the parameters directly when executing, overriding the properties files
e.g.:

./gradlew bootRun -Dspring.profiles.active=dev -Pargs='--spring.data.mongodb.uri=mongodb://localhost:27017/whatever'

h2. Caveats & possible improvements

While working on the solution, I have identified a lot of possible improvements and quick-wins, but chose not to do them mainly to keep the focus on the important stuff. The list of most important/obivous improvements follows:

- database transactions (using @Transactional) as not implemented > because it requires that Mongo DB uses replica sets
- web application is very simple, not using any CSS nor any JS framework > aimed to backend development
- web application not API endpoints are not secured
- Dockerfile is not written using multi stage build, because in would dramatically increase build time (or would require additional setup to use host's gradle cache)
- conversion between Zonky API and domain objects may be done using existing frameworks (possible evenusing Spring's Converter)
