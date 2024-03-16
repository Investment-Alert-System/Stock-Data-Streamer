FROM maven:3.8.3-openjdk-17 AS build
LABEL authors="philipppollmann"

COPY src /home/microservice/src
COPY pom.xml /home/microservice
COPY mvnw /home/microservice
COPY mvnw.cmd /home/microservice

RUN mvn -f /home/microservice/pom.xml clean package -B

FROM maven:3.8.3-openjdk-17

COPY --from=build /home/microservice/target/*.jar /usr/local/lib/sds-ms.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/sds-ms.jar"]