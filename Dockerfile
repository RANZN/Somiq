# Stage 1: Build the application using a JDK image
FROM eclipse-temurin:17-jdk-focal as builder

# Set the working directory to the root of the app
WORKDIR /app

# Copy all the essential Gradle files from the project root.
# This is why this Dockerfile must be in the root.
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy the source code for the server module.
COPY server/ ./server
# If your server depends on a 'shared' module, you must copy it as well.
# COPY shared/ ./shared

# Make the Gradle wrapper executable and build the :server module specifically.
# This command tells Gradle, from the root, to build only the 'server' subproject.
RUN chmod +x ./gradlew && ./gradlew :server:build -x test

# Stage 2: Create a minimal final image for running the application
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copy the final fat JAR from the correct build output directory inside the server module.
COPY --from=builder /app/server/build/libs/*-all.jar app.jar

EXPOSE 8080

# Run the application using the production config file.
ENTRYPOINT ["java", "-jar", "app.jar", "-config=application-prod.conf"]

