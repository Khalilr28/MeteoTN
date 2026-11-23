FROM eclipse-temurin:21-jre-jammy
RUN useradd --system --uid 10001 --create-home appuser
WORKDIR /app
COPY target/meteotn-1.0.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]