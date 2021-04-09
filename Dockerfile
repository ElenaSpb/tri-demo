FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=/build/libs/demo-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","app.jar"]