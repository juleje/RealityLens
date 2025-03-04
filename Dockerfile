FROM gradle:jdk11 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
EXPOSE 8080
RUN gradle build --stacktrace
ENTRYPOINT gradle bootRun