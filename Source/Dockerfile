FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
RUN mkdir -p /app/photos && chmod -R 775 /app/photos
COPY --from=build /app/target/Bring-Me-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

