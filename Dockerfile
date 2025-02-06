FROM cloudnativek8s/microservices-java-alpine:v1.0.30

ENV LANGUAGE='en_US:en'

RUN chown -R app:app /opt/app && chmod -R g+rwx /opt/app
USER app
# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=app:app  app/build/libs/app.jar /opt/app/app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
CMD [ "java", "-jar", "/opt/app/app.jar" ]



