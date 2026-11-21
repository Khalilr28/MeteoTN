FROM eclipse-temurin:21-jre-jammy
RUN useradd --system --create-home appuser
WORKDIR /app
COPY target/meteotn-1.0.0.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]