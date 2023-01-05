FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=repository/ru/artemev/producer/0.0.1-SNAPSHOT/producer-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} producer-0.0.1-SNAPSHOT.jar
COPY ./kafka-app-demo/kafka-load-producer/src/main/resources/application.properties application.yml
ENTRYPOINT ["java", "-jar", "producer-0.0.1-SNAPSHOT.jar"]
