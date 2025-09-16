# Stage 1: Build the application using a JDK image
FROM eclipse-temurin:17-jdk-focal as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy the source code
COPY src ./src

# Make the Gradle wrapper executable and build the fat JAR
RUN chmod +x ./gradlew && ./gradlew build -x test

# Stage 2: Create a minimal final image to run the application
FROM eclipse-temurin:17-jre-focal

# Set the working directory
WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /app/build/libs/*-all.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# The command to run the application when the container starts.
# It explicitly tells Ktor to use the production configuration file.
ENTRYPOINT ["java", "-jar", "app.jar", "-config=application-prod.conf"]

