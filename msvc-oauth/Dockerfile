FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

EXPOSE 9100

ADD ./target/msvc-oauth-0.0.1-SNAPSHOT.jar oauth-server.jar

ENTRYPOINT ["java", "-jar", "oauth-server.jar"]