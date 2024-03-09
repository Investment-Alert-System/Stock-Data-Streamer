FROM maven:3.8.3-openjdk-17 AS build
LABEL authors="philipppollmann"

COPY src /home/app/src
COPY pom.xml /home/app
COPY mvnw /home/app
COPY mvnw.cmd /home/app

RUN mvn -f /home/app/pom.xml clean package -B

FROM maven:3.8.3-openjdk-17

COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]