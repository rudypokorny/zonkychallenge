FROM adoptopenjdk/openjdk11:alpine-jre
RUN mkdir -p /opt/zonkychallenge
WORKDIR /opt/zonkychallenge
COPY /build/libs/zonkychallenge-*.jar ./zonkychallenge.jar

COPY /docker/wait-for.sh .
RUN chmod +x ./wait-for.sh

EXPOSE 8080
CMD ["java", "-jar", "zonkychallenge.jar"]