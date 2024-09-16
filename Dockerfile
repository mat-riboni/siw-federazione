FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/ascii-art-generator-0.0.1-SNAPSHOT.jar ascii-art-generator.jar
ENTRYPOINT ["java","-jar","/siw-federazione.jar"]
EXPOSE 8080