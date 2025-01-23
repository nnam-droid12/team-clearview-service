FROM openjdk:17-jdk-alpine
ADD target/docusign-hackathon-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/credentials.json /app/credentials.json

ENTRYPOINT ["java","-jar","/app.jar"]
