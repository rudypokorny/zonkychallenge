
spring profile - dev > uses ín memory mongo db instance
spring rpofule prod > uses externally hosted mongo db instance (in pir case in docker container)


h2.Running using Docker
docker-compose up --build

h2.Running locally Gradle
./gradlew bootRun -Pargs='--spring.data.mongodb.host=<dbHost>'

<dbHost> - address where the database is hosted: Defaulted to 'localhost'.

You may also specify
dbHost - database port. Defautled to '27017'.
dbName - database name. Defaulted to 'zonkyLoans'

Features
- highly confiugurable
- persisted results 
- web interface for dispalying results and aggregations
- cloud ready


