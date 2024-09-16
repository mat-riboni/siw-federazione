FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/siw-federazione-0.0.1-SNAPSHOT.jar siw-federazione.jar
ENTRYPOINT ["java","-jar","/siw-federazione.jar"]
EXPOSE 8080