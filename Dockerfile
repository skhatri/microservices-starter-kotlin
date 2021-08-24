FROM openjdk:11.0.6-jdk
RUN mkdir -p /opt/app
COPY app/build/libs/app.jar /opt/app/
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/app.jar"]

