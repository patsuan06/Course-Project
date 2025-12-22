# Use an official OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set work directory
WORKDIR /app

# Copy Maven build files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the app
RUN ./mvnw package -DskipTests

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","target/courier-app-0.0.1-SNAPSHOT.jar"]
