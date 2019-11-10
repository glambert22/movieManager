FROM openjdk:11-jre-slim

EXPOSE 9001

ARG JAR_FILE
COPY ${JAR_FILE} /app.jar

ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} -jar /app.jar