FROM openjdk:8-jdk as builder
WORKDIR /app
COPY ./src/*.java ./
RUN javac *.java
RUN ls -l

FROM openjdk:8-jre
WORKDIR /app
COPY --from=builder ./app/*.class ./
EXPOSE 54321
CMD ["java", "Server"]
