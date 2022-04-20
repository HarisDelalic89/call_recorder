FROM gradle:7-jdk8 as builder
WORKDIR /app
COPY . .
RUN ./gradlew build --stacktrace

FROM openjdk
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/build/libs/call_recorder-1.jar .
CMD java -jar call_recorder-1.jar