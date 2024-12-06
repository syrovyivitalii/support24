# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/support24-0.0.1-SNAPSHOT.jar support24.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8085

# Set the timezone
ENV TZ=Europe/Kiev

# Run the application
ENTRYPOINT ["java", "-Duser.timezone=Europe/Kiev", "-jar", "support24.jar"]