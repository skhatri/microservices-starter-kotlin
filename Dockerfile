FROM openjdk:21-jdk-slim
RUN mkdir -p /opt/app
COPY app/build/libs/app.jar /opt/app/
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/app.jar"]

