# Use an official OpenJDK runtime as a parent image
FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/course-service-0.0.1-SNAPSHOT.jar /app/course-service.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the JAR file
ENTRYPOINT ["java", "-jar", "course-service.jar"]
