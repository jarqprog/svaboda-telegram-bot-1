FROM openjdk:14-oracle AS builder

ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle $APP_HOME
COPY settings.gradle $APP_HOME
COPY gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM openjdk:14-oracle
ENV APP_HOME=/app
ENV ARTIFACT_NAME="monitor-1.0.0-SNAPSHOT.jar"
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/monitor/build/libs/$ARTIFACT_NAME .
ADD "/monitor/run.sh" "/"
RUN chmod +x "/run.sh"

# setting env vars - not relevant for deployment with heroku
ARG _port=$_port
ARG _intervalSec=$_intervalSec
ARG _servicesBaseUrls=$_servicesBaseUrls
ENV PORT=$_port
ENV INTERVAL_SEC=$_intervalSec
ENV SERVICES_BASE_URLS=$_servicesBaseUrls

EXPOSE $PORT

ENTRYPOINT ["/run.sh"]