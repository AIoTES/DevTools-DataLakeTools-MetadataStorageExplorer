FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    SIMLIFE_SLEEP=0 \
    JAVA_OPTS=""

# Add a simlife user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh simlife
USER simlife

WORKDIR /home/simlife

COPY --chown=simlife entrypoint.sh entrypoint.sh
COPY --chown=simlife *.war app.war

ENTRYPOINT ["sh", "./entrypoint.sh"]

EXPOSE 8080
