FROM gradle:6.0-jdk11 AS BUILDER
COPY --chown=gradle:gradle . /home/gradle/zonkychallenge
WORKDIR /home/gradle/zonkychallenge
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk11:alpine-jre
RUN mkdir -p /opt/zonkychallenge
WORKDIR /opt/zonkychallenge
COPY --from=BUILDER /home/gradle/zonkychallenge/build/libs/zonkychallenge-*.jar ./zonkychallenge.jar

COPY /docker/wait-for.sh .
RUN chmod +x ./wait-for.sh && \
    apk add --update dos2unix && \
    dos2unix ./wait-for.sh

EXPOSE 8080
CMD ["java", "-jar", "zonkychallenge.jar"]
