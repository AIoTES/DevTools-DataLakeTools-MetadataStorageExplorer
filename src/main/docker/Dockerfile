FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    SIMLIFE_SLEEP=0 \
    JAVA_OPTS=""

CMD echo "The application will start in ${SIMLIFE_SLEEP}s..." && \
    sleep ${SIMLIFE_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war

EXPOSE 8093

ADD *.war /app.war
