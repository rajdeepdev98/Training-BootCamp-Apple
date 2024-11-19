# Use an official Scala image as a parent image
FROM hseeberger/scala-sbt:11.0.12_1.5.5_2.13.6

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app


# Build the project and stage it
RUN sbt  clean compile stage

# Make port 8080 available to the world outside this container
EXPOSE 8081

# Define environment variable
ENV NAME = play-event-service

#Entry point to the application
ENTRYPOINT ["./target/universal/stage/bin/play-event-service"]
