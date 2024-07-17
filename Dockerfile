FROM alpine/java:21-jre

RUN adduser -D nonroot
WORKDIR /home/nonroot

COPY ../build/libs/application.jar application.jar

USER nonroot
CMD ["java", "-jar", "application.jar"]