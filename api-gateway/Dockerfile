FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

EXPOSE 8090

ADD ./target/api-gateway-0.0.1-SNAPSHOT.jar gateway-server.jar

ENTRYPOINT ["java", "-jar", "gateway-server.jar"]