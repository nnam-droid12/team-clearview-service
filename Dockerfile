FROM openjdk:17-jdk-alpine
COPY target/docusign-hackathon-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/tokens
RUN echo $GOOGLE_CREDENTIALS_BASE64 | base64 -d > /app/credentials.json
RUN chmod -R 777 /app/tokens

ENTRYPOINT ["java","-jar","/app.jar"]