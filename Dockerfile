FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=repository/ru/artemev/cosumer/0.0.1-SNAPSHOT/cosumer-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} cosumer-0.0.1-SNAPSHOT.jar
COPY ./kafka-app-demo/kafka-load-consumer/src/main/resources/application.properties application.yml
ENTRYPOINT ["java", "-jar", "cosumer-0.0.1-SNAPSHOT.jar"]
